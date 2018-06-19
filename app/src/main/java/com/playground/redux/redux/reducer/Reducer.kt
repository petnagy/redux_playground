package com.playground.redux.redux.reducer

import com.playground.redux.redux.actions.*
import com.playground.redux.redux.appstate.*
import com.playground.redux.redux_impl.Action

fun appReducer(action: Action, state: AppState): AppState = AppState(
        user = userReducer(action, state.user),
        pageState = navigationReducer(action, state.pageState),
        repos = repoReducer(action, state.repos),
        commits = commitsReducer(action, state.commits)
)

fun userReducer(action: Action, userState: UserState): UserState {
    var state = userState
    when(action) {
        is LoadPreviousSearchAction -> state = state.copy(loading = true)
        is PreviousSearchListAction -> state = state.copy(loading = false, history = action.prevUserSearches.map { it.userName }.toList())
        is SelectUserAction -> state = state.copy(selectedUserName = action.selectedUser)
        is UserTypeAction -> state = state.copy(typedName = action.typedText)
        is AddHistoryAction -> {
            val historyList = state.history.toMutableList()
            historyList.remove(action.selectedUser)
            historyList.add(0, action.selectedUser)
            state = state.copy(history = historyList.toList())
        }
        is HistoryItemDeleteAction -> {
            val historyList = state.history.toMutableList()
            historyList.remove(action.userName)
            state = state.copy(history = historyList.toList())
        }
    }
    return state
}

fun navigationReducer(action: Action, pageState: PageState): PageState {
    var state = pageState
    when(action) {
        is NextPageAction -> state = state.copy(actualPage = action.page)
    }
    return state
}

fun repoReducer(action: Action, repoState: RepoState): RepoState {
    var state = repoState
    when(action) {
        is LoadReposAction -> state = state.copy(loading = true)
        is GitHubReposSuccessAction -> state = state.copy(loading = false, repoList = action.repoList)
        is GitHubReposFailedAction -> state = state.copy(loading = false, repoList = emptyList())
        is ClearRepoItemsAction -> state = state.copy(repoList = emptyList())
        is SetFavouriteAction -> {
            val list = repoState.repoList.map { it -> if (it.name == action.repoName) it.copy(favorite = true) else it}
            state = state.copy(repoList = list)
        }
        is ClearFavouriteAction -> {
            val list = repoState.repoList.map { it -> if (it.name == action.repoName) it.copy(favorite = false) else it}
            state = state.copy(repoList = list)
        }
        is FavouriteLoadedFromDbAction -> {
            val updatedList = repoState.repoList.map { it -> it.copy(favorite = action.resultMap.contains(it.name)) }.toList()
            state = state.copy(repoList = updatedList)
        }
        is RepoSelectedAction -> state = state.copy(selectedRepoName = action.repoName)
    }
    return state
}

fun commitsReducer(action: Action, commits: CommitState): CommitState {
    var state = commits
    when(action) {
        is LoadCommitsAction -> state = state.copy(loading = true)
        is CommitsLoadedFailedAction -> state = state.copy(loading = false, commitList = emptyList())
        is CommitsLoadedSuccessAction -> state = state.copy(loading = false, commitList = action.commits)
        is ClearCommitListAction -> state = state.copy(commitList = emptyList())
    }
    return state
}