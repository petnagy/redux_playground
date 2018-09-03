package com.playground.redux.redux.middlewares

import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.playground.redux.redux.actions.UserTypeAction
import com.playground.redux.redux.appstate.AppState
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