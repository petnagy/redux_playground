package com.playground.redux.actions

import com.playground.redux.data.GitHubRepo
import tw.geothings.rekotlin.Action

class LoadReposAction(val userName: String): Action

class GitHubReposSuccessAction(val repoList: List<GitHubRepo>): Action

class GitHubReposFailedAction: Action

class LoadFavouriteInfoFromDbAction(val repoList: List<GitHubRepo>, val userName: String): Action

class RemoveFavouriteAction(val userName: String, val repoName: String): Action

class SaveFavouriteAction(val userName: String, val repoName: String): Action

class ClearRepoItemsAction(): Action