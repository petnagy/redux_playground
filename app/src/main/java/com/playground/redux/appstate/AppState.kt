package com.playground.redux.appstate

import com.playground.redux.navigation.Page
import tw.geothings.rekotlin.StateType

data class AppState(val user: UserState, val actualPage: Page, val repos: RepoState): StateType