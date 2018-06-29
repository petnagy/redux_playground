package com.playground.redux.redux.middlewares

import com.playground.redux.data.GitHubRepo
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.actions.*
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Middleware
import com.playground.redux.redux_impl.Store
import com.playground.redux.repository.Repository
import com.playground.redux.repository.gitrepo.GitRepoSpecification
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Named

fun reposMiddleware(endpoint: GitHubEndpoint, @Named("GIT_REPO") repository: Repository<GitHubRepoEntity>): Middleware<AppState> = { store, action, next ->
    when (action) {
        is LoadReposAction -> {
            endpoint.getRepos(action.userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result -> handleGitHubRepoResult(store, result) },
                            { error -> handleGitHubRepoError(store, error.message) }
                    )
        }
        is LoadFavouriteInfoFromDbAction -> handleLoadFromDatabase(store, repository, action.userName)
        is SaveFavouriteAction -> {
            repository.add(GitHubRepoEntity(action.userName, action.repoName))
            store.dispatch(SetFavouriteAction(action.repoName))
        }
        is RemoveFavouriteAction -> {
            repository.remove(GitHubRepoEntity(action.userName, action.repoName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            store.dispatch(ClearFavouriteAction(action.repoName))
        }
    }
    next.dispatch(action)
}

fun handleLoadFromDatabase(store: Store<AppState>, repository: Repository<GitHubRepoEntity>, userName: String) {
    repository.query(GitRepoSpecification(userName)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result -> handleSuccessLoadFromDb(store, result) },
                    { error -> handleGitHubRepoError(store, "Error loading from DB " + error.message) }
            )
}

fun handleSuccessLoadFromDb(store: Store<AppState>, result: List<GitHubRepoEntity>) {
    val resultMap = result.map { it.repoName to it }.toMap()
    store.dispatch(FavouriteLoadedFromDbAction(resultMap))
}

fun handleGitHubRepoResult(store: Store<AppState>, repoList: List<GitHubRepo>) {
    store.dispatch(GitHubReposSuccessAction(repoList))
    val userName = if (repoList.isNotEmpty()) repoList[0].owner.login else ""
    //TODO no repos error message
    store.dispatch(LoadFavouriteInfoFromDbAction(userName))
    Timber.d("GitHubRepos success Network Call")
}

fun handleGitHubRepoError(store: Store<AppState>, message: String?) {
    store.dispatch(GitHubReposFailedAction())
    Timber.e(message)
}
