package com.playground.redux.middlewares

import com.playground.redux.actions.AddHistoryAction
import com.playground.redux.actions.SelectUserAction
import com.playground.redux.appstate.AppState
import tw.geothings.rekotlin.Middleware

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