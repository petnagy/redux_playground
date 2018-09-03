package com.playground.redux.redux.actions

import com.petnagy.koredux.Action
import com.playground.redux.data.GitCommit

class LoadCommitsAction(val userName: String, val repoName: String): Action

class CommitsLoadedSuccessAction(val commits: List<GitCommit>): Action

class CommitsLoadedFailedAction: Action

class ClearCommitListAction: Action