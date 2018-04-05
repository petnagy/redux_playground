package com.playground.redux.middlewares

import com.playground.redux.actions.ChangeGitHubUser
import com.playground.redux.actions.NextPageAction
import com.playground.redux.appstate.AppState
import com.playground.redux.pages.GitHubReposActivity
import tw.geothings.rekotlin.Middleware

internal val navigatorMiddleware: Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is ChangeGitHubUser -> dispatch(NextPageAction(GitHubReposActivity::class.java))
            }
            next(action)
        }
    }
}
