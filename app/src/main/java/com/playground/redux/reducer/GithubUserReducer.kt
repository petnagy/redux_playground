package com.playground.redux.reducer

import com.playground.redux.actions.ChangeGitHubUser
import com.playground.redux.appstate.GithubUserState
import tw.geothings.rekotlin.Action

fun githubUserReducer(action: Action, githubUserState: GithubUserState?): GithubUserState {
    var state = githubUserState ?: GithubUserState()
    when(action) {
        is ChangeGitHubUser -> state = state.copy(selectedUserName = action.gitHubUser)
    }
    return state
}