package com.playground.redux.redux.actions

import com.playground.redux.data.GitHubRepo
import com.playground.redux.data.GitHubRepoEntity
import tw.geothings.rekotlin.Action

class LoadReposAction(val userName: String): Action

class GitHubReposSuccessAction(val repoList: List<GitHubRepo>): Action

class GitHubReposFailedAction: Action

class LoadFavouriteInfoFromDbAction(val userName: String): Action

class RemoveFavouriteAction(val userName: String, val repoName: String): Action

class SaveFavouriteAction(val userName: String, val repoName: String): Action

class ClearRepoItemsAction: Action

class SetFavouriteAction(val repoName: String): Action

class ClearFavouriteAction(val repoName: String): Action

class FavouriteLoadedFromDbAction(val resultMap: Map<String, GitHubRepoEntity>): Action

class RepoSelectedAction(val repoName: String): Action