package com.playground.redux.network

import com.playground.redux.data.GitHubRepo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubEndpoint {

    @GET("users/{userName}/repos")
    fun getRepos(@Path("userName") userName: String): Single<List<GitHubRepo>>

}