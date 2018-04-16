package com.playground.redux.appstate

import com.playground.redux.data.GitHubRepo
import tw.geothings.rekotlin.StateType

data class RepoState(val loading: Boolean = false, val repoList: List<GitHubRepo> = emptyList()): StateType