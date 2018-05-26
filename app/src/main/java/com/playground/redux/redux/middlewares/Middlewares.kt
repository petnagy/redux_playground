package com.playground.redux.redux.middlewares

import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.redux.actions.NextPageAction
import com.playground.redux.redux.actions.RepoSelectedAction
import com.playground.redux.redux.actions.SelectUserAction
import com.playground.redux.redux.actions.UserTypeAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Middleware
import timber.log.Timber

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
