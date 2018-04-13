package com.playground.redux.middlewares

import com.playground.redux.actions.AddHistoryAction
import com.playground.redux.actions.NextPageAction
import com.playground.redux.actions.SelectUserAction
import com.playground.redux.appstate.AppState
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import tw.geothings.rekotlin.Middleware

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