package com.playground.redux.redux.actions

import com.playground.redux.data.GitCommit
import com.playground.redux.redux_impl.Action

class LoadCommitsAction(val userName: String, val repoName: String): Action

class CommitsLoadedSuccessAction(val commits: List<GitCommit>): Action

class CommitsLoadedFailedAction: Action

class ClearCommitListAction: Action