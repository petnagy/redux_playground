package com.playground.redux.redux.middlewares

import android.annotation.SuppressLint
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.playground.redux.data.GitHubRepo
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.actions.*
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.repository.Repository
import com.playground.redux.repository.gitrepo.GitRepoSpecification
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RepoMiddleware(private val endpoint: GitHubEndpoint,
                     private val repository: Repository<GitHubRepoEntity>): Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is LoadReposAction -> loadRepos(action, store)
            is LoadFavouriteInfoFromDbAction -> handleLoadFromDatabase(store, repository, action.userName)
            is SaveFavouriteAction -> saveFavourite(action, store)
            is RemoveFavouriteAction -> removeFavourite(action, store)
        }
        next.dispatch(action)
    }

    private fun removeFavourite(action: RemoveFavouriteAction, store: Store<AppState>) {
        repository.remove(GitHubRepoEntity(action.userName, action.repoName))
                .doOnComplete { store.dispatch(ClearFavouriteAction(action.repoName)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun saveFavourite(action: SaveFavouriteAction, store: Store<AppState>) {
        repository.add(GitHubRepoEntity(action.userName, action.repoName))
                .doOnComplete { store.dispatch(SetFavouriteAction(action.repoName)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    @SuppressLint("CheckResult")
    private fun loadRepos(action: LoadReposAction, store: Store<AppState>) {
        endpoint.getRepos(action.userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> handleGitHubRepoResult(store, result) },
                        { error -> handleGitHubRepoError(store, error.message) }
                )
    }

    @SuppressLint("CheckResult")
    private fun handleLoadFromDatabase(store: Store<AppState>, repository: Repository<GitHubRepoEntity>, userName: String) {
        repository.query(GitRepoSpecification(userName)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> handleSuccessLoadFromDb(store, result) },
                        { error -> handleGitHubRepoError(store, "Error loading from DB " + error.message) }
                )
    }

    private fun handleSuccessLoadFromDb(store: Store<AppState>, result: List<GitHubRepoEntity>) {
        val resultMap = result.map { it.repoName to it }.toMap()
        store.dispatch(FavouriteLoadedFromDbAction(resultMap))
    }

    private fun handleGitHubRepoResult(store: Store<AppState>, repoList: List<GitHubRepo>) {
        store.dispatch(GitHubReposSuccessAction(repoList))
        val userName = if (repoList.isNotEmpty()) repoList[0].owner.login else ""
        //TODO no repos error message
        store.dispatch(LoadFavouriteInfoFromDbAction(userName))
        Timber.d("GitHubRepos success Network Call")
    }

    private fun handleGitHubRepoError(store: Store<AppState>, message: String?) {
        store.dispatch(GitHubReposFailedAction())
        Timber.e(message)
    }

}
