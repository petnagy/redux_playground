package com.playground.redux.redux.appstate

data class UserState(val selectedUserName: String = "",
                     val typedName: String = "",
                     val history: List<String> = emptyList(),
                     val loading: Boolean = false)