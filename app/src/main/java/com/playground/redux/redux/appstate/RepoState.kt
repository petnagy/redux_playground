package com.playground.redux.redux.appstate

import com.playground.redux.data.GitHubRepo

data class RepoState(val loading: Boolean = false, val repoList: List<GitHubRepo> = emptyList(), val selectedRepoName: String = "")