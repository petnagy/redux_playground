package com.playground.redux.redux.middlewares

import android.annotation.SuppressLint
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.playground.redux.data.GitCommit
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.actions.CommitsLoadedFailedAction
import com.playground.redux.redux.actions.CommitsLoadedSuccessAction
import com.playground.redux.redux.actions.LoadCommitsAction
import com.playground.redux.redux.appstate.AppState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CommitMiddleware(private val endpoint: GitHubEndpoint): Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is LoadCommitsAction -> loadCommits(action, store)
        }
        next.dispatch(action)
    }

    @SuppressLint("CheckResult")
    private fun loadCommits(action: LoadCommitsAction, store: Store<AppState>) {
        endpoint.getCommits(action.userName, action.repoName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> handleGitHubCommitsResult(store, result) },
                        { error -> handleGitHubCommitsError(store, error.message) }
                )
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

