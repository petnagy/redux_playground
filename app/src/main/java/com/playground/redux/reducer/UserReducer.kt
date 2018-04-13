package com.playground.redux.reducer

import com.playground.redux.actions.AddHistoryAction
import com.playground.redux.actions.SelectUserAction
import com.playground.redux.actions.UserTypeAction
import com.playground.redux.appstate.UserState
import tw.geothings.rekotlin.Action

fun userReducer(action: Action, userState: UserState): UserState {
    var state = userState
    when(action) {
        is SelectUserAction -> state = state.copy(selectedUserName = action.selectedUser)
        is UserTypeAction -> state = state.copy(typedName = action.typedText)
        is AddHistoryAction -> {
            val historyList = state.history.toMutableSet()
            historyList.add(action.selectedUser)
            state = state.copy(history = historyList.toList())
        }
    }
    return state
}