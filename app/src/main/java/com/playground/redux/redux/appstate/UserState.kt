package com.playground.redux.redux.appstate

import tw.geothings.rekotlin.StateType

data class UserState(val selectedUserName: String = "", val typedName: String = "", val history: List<String> = emptyList()): StateType