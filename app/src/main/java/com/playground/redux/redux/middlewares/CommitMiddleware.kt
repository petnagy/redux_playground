package com.playground.redux.redux.middlewares

import com.playground.redux.data.GitCommit
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.actions.CommitsLoadedFailedAction
import com.playground.redux.redux.actions.CommitsLoadedSuccessAction
import com.playground.redux.redux.actions.LoadCommitsAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Action
import com.playground.redux.redux_impl.DispatchFunction
import com.playground.redux.redux_impl.Middleware
import com.playground.redux.redux_impl.Store
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CommitMiddleware(private val endpoint: GitHubEndpoint): Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
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

    private fun handleGitHubCommitsResult(store: Store<AppState>, result: List<GitCommit>) {
        Timber.d("Commits load from net Success")
        store.dispatch(CommitsLoadedSuccessAction(result))
    }

    private fun handleGitHubCommitsError(store: Store<AppState>, message: String?) {
        Timber.e(message)
        store.dispatch(CommitsLoadedFailedAction())
    }
}

