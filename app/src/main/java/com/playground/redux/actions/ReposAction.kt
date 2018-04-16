package com.playground.redux.actions

import com.playground.redux.data.GitHubRepo
import tw.geothings.rekotlin.Action

class LoadReposAction(val userName: String): Action

class GitHubReposSuccessAction(val repoList: List<GitHubRepo>): Action

class GitHubReposFailedAction: Action