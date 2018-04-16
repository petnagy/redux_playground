package com.playground.redux.pages.repospage.viewmodel

import android.databinding.Bindable
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.data.GitHubRepo

class RepoItemViewModel(private val repo: GitHubRepo): ListItemViewModel() {

    override fun getViewType() = 435111

    @Bindable
    fun getRepoName() = repo.name

    @Bindable
    fun getLanguage() = repo.language

    @Bindable
    fun isFavorite() = false
}