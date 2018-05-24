package com.playground.redux.redux.middlewares

import com.playground.redux.data.GitCommit
import com.playground.redux.data.GitHubRepo
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
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

internal val loggingMiddleware: Middleware<AppState> = { _, action, next ->
    var log = "Action: -> " + action::class.java.simpleName
    when (action) {
        is UserTypeAction -> log += " typed: " + action.typedText
    }
    Timber.d(log)
    next.dispatch(action)
}

fun navigationMiddleware(navigator: Navigator): Middleware<AppState> = { store, action, next ->
    when (action) {
        is SelectUserAction -> store.dispatch(NextPageAction(navigator.goNextPage(Page.USER_SELECT_PAGE)))
        is RepoSelectedAction -> store.dispatch(NextPageAction(navigator.goNextPage(Page.REPO_SELECT_PAGE)))
    }
    next.dispatch(action)
}

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

internal val userMiddleware: Middleware<AppState> = { store, action, next ->
    when (action) {
        is SelectUserAction -> store.dispatch(AddHistoryAction(action.selectedUser))
    }
    next.dispatch(action)
}

fun commitsMiddleware(endpoint: GitHubEndpoint, @Named("GIT_REPO") repository: Repository<GitHubRepoEntity>): Middleware<AppState> = { store, action, next ->
    when (action) {
        is LoadCommitsAction -> {
            endpoint.getCommits(action.userName, action.repoName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result -> handleGitHubCommitsResult(store, result) },
                            { error -> handleGitHubCommitsError(store, error.message) }
                    )
        }
    }
    next.dispatch(action)
}

fun handleGitHubCommitsResult(store: Store<AppState>, result: List<GitCommit>) {
    Timber.d("Commits load from net Success")
    store.dispatch(CommitsLoadedSuccessAction(result))
}

fun handleGitHubCommitsError(store: Store<AppState>, message: String?) {
    Timber.e(message)
    store.dispatch(CommitsLoadedFailedAction())
}
