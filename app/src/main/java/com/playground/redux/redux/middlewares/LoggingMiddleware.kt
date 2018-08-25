package com.playground.redux.redux.middlewares

import com.playground.redux.redux.actions.UserTypeAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Action
import com.playground.redux.redux_impl.DispatchFunction
import com.playground.redux.redux_impl.Middleware
import com.playground.redux.redux_impl.Store
import timber.log.Timber

class LoggingMiddleware: Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        var log = "Action: -> " + action::class.java.simpleName
        when (action) {
            is UserTypeAction -> log += " typed: " + action.typedText
        }
        Timber.d(log)
        next.dispatch(action)
    }

}