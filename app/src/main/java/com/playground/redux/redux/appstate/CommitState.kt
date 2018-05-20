package com.playground.redux.redux.appstate

import com.playground.redux.data.GitCommit

data class CommitState(val commitList: List<GitCommit> = emptyList(), val loading: Boolean = false)