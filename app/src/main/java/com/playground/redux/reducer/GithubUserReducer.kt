package com.playground.redux.reducer

import com.playground.redux.actions.AddHistoryAction
import com.playground.redux.actions.SelectGitHubUserAction
import com.playground.redux.actions.UserTypeAction
import com.playground.redux.appstate.GithubUserState
import tw.geothings.rekotlin.Action

fun githubUserReducer(action: Action, githubUserState: GithubUserState): GithubUserState {
    var state = githubUserState
    when(action) {
        is SelectGitHubUserAction -> state = state.copy(selectedUserName = action.gitHubUser)
        is UserTypeAction -> state = state.copy(typedName = action.typedText)
        is AddHistoryAction -> {
            val historyList = state.history.toMutableSet()
            historyList.add(action.gitHubUser)
            state = state.copy(history = historyList.toList())
        }
    }
    return state
}