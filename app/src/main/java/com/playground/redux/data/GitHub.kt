package com.playground.redux.data

import android.arch.persistence.room.Entity

data class GitHubOwner(val login: String, val id: Long)

data class GitHubRepo(val id: Int, val name: String, val html_url: String, val language: String?, val owner: GitHubOwner, val favorite: Boolean)

data class GitCommit(val commit: Commit)

data class Commit(val message:String, val committer: Committer)

data class Committer(val name: String, val email: String, val date: String)

@Entity(tableName = "favourite_git_repo", primaryKeys = ["userName", "repoName"])
data class GitHubRepoEntity(val userName: String, val repoName: String)