package com.playground.redux.appstate

import tw.geothings.rekotlin.StateType

data class AppState(val githubUser: GithubUserState? = null): StateType