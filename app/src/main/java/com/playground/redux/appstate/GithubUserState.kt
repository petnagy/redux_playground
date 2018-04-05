package com.playground.redux.appstate

import tw.geothings.rekotlin.StateType

data class GithubUserState(val selectedUserName: String = "", val typedName: String = "", val history: List<String> = emptyList()): StateType