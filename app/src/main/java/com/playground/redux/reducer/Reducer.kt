package com.playground.redux.reducer

import com.playground.redux.appstate.AppState
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState = AppState(
        githubUser = githubUserReducer(action, state!!.githubUser)
)