package com.playground.redux.redux.appstate

import com.playground.redux.data.UserSearch

data class UserState(val selectedUserName: String = "",
                     val typedName: String = "",
                     val history: List<UserSearch> = emptyList(),
                     val loading: Boolean = false)