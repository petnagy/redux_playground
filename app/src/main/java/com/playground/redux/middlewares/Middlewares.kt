package com.playground.redux.middlewares

import com.playground.redux.actions.*
import com.playground.redux.appstate.AppState
import com.playground.redux.data.GitHubRepo
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.network.GitHubEndpoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import tw.geothings.rekotlin.DispatchFunction
import tw.geothings.rekotlin.Middleware

internal val loggingMiddleware: Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            Timber.d("Action: -> " + action::class.java.simpleName)
            next(action)
        }
    }
}

fun navigationMiddleware(navigator: Navigator): Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is SelectUserAction -> dispatch(NextPageAction(navigator.goNextPage(Page.USER_SELECT_PAGE)))
            }
            next(action)
        }
    }
}

fun reposMiddleware(endpoint: GitHubEndpoint): Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is LoadReposAction -> {
                    endpoint.getRepos(action.userName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result -> handleGitHubRepoResult(dispatch, result) },
                                    { error -> handleGitHubRepoError(dispatch, error.message)}
                            )
                }
            }
            next(action)
        }
    }
}

fun handleGitHubRepoResult(dispatch: DispatchFunction, repoList: List<GitHubRepo>) {
    dispatch(GitHubReposSuccessAction(repoList))
    Timber.d("GitHubRepos success Network Call")
}

fun handleGitHubRepoError(dispatch: DispatchFunction, message: String?) {
    dispatch(GitHubReposFailedAction())
    Timber.e(message)
}

internal val userMiddleware: Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is SelectUserAction -> dispatch(AddHistoryAction(action.selectedUser))
            }
            next(action)
        }
    }
}