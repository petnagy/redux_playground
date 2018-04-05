package com.playground.redux.middlewares

import com.playground.redux.actions.AddHistoryAction
import com.playground.redux.actions.SelectGitHubUserAction
import com.playground.redux.appstate.AppState
import tw.geothings.rekotlin.Middleware

internal val githubUserMiddleware: Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is SelectGitHubUserAction -> dispatch(AddHistoryAction(action.gitHubUser))
            }
            next(action)
        }
    }
}