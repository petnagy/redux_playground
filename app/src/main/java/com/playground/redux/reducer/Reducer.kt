package com.playground.redux.reducer

import com.playground.redux.actions.*
import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.RepoState
import com.playground.redux.appstate.UserState
import com.playground.redux.navigation.Page
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState = AppState(
        user = userReducer(action, state!!.user),
        actualPage = navigationReducer(action, state.actualPage),
        repos = repoReducer(action, state.repos)
)

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

fun navigationReducer(action: Action, pageState: Page): Page {
    var state = pageState
    when(action) {
        is NextPageAction -> state = action.page
    }
    return state
}

fun repoReducer(action: Action, repoState: RepoState): RepoState {
    var state = repoState
    when(action) {
        is LoadReposAction -> state = state.copy(loading = true)
        is GitHubReposSuccessAction -> state = state.copy(loading = false, repoList = action.repoList)
        is GitHubReposFailedAction -> state = state.copy(loading = false)
        is ClearRepoItemsAction -> state = state.copy(repoList = emptyList())
    }
    return state
}