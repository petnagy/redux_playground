package com.playground.redux.redux.appstate

import tw.geothings.rekotlin.StateType

data class AppState(val user: UserState, val pageState: PageState, val repos: RepoState, val commits: CommitState): StateType