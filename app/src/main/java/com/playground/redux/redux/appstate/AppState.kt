package com.playground.redux.redux.appstate

data class AppState(val user: UserState, val pageState: PageState, val repos: RepoState, val commits: CommitState)