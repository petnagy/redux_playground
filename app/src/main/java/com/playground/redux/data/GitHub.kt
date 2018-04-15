package com.playground.redux.data

data class GitHubOwner(val login: String, val id: Long)

data class GitHubRepo(val id: Int, val name: String, val html_url: String, val language: String, val owner: GitHubOwner)