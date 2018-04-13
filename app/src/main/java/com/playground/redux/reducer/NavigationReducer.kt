package com.playground.redux.reducer

import com.playground.redux.actions.NextPageAction
import com.playground.redux.navigation.Page
import tw.geothings.rekotlin.Action

fun navigationReducer(action: Action, pageState: Page): Page {
    var state = pageState
    when(action) {
        is NextPageAction -> state = action.page
    }
    return state
}