package com.playground.redux.reducer

import com.playground.redux.appstate.AppState
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState = AppState(
        user = userReducer(action, state!!.user), actualPage = navigationReducer(action, state.actualPage)
)