package com.playground.redux.redux.middlewares

import com.playground.redux.redux.actions.AddHistoryAction
import com.playground.redux.redux.actions.SelectUserAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Middleware

internal val userMiddleware: Middleware<AppState> = { store, action, next ->
    when (action) {
        is SelectUserAction -> store.dispatch(AddHistoryAction(action.selectedUser))
    }
    next.dispatch(action)
}