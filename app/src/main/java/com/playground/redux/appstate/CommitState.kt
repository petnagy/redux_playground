package com.playground.redux.appstate

import com.playground.redux.data.GitCommit
import tw.geothings.rekotlin.StateType

data class CommitState(val commitList: List<GitCommit> = emptyList(), val loading: Boolean = false): StateType