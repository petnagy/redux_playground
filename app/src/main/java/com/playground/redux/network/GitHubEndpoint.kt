package com.playground.redux.network

import com.playground.redux.data.GitHubRepo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import com.playground.redux.data.GitCommit




interface GitHubEndpoint {

    @GET("users/{userName}/repos")
    fun getRepos(@Path("userName") userName: String): Single<List<GitHubRepo>>

    @GET("repos/{userName}/{repo}/commits")
    fun getCommits(@Path("userName") userName: String, @Path("repo") repo: String): Single<List<GitCommit>>
}