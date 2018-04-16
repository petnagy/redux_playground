package com.playground.redux.pages.repospage.viewmodel

import android.databinding.Bindable
import android.view.View
import com.playground.redux.appstate.AppState
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.data.GitHubRepo
import tw.geothings.rekotlin.Store

class RepoItemViewModel(private val repo: GitHubRepo, val store: Store<AppState>): ListItemViewModel() {

    override fun getViewType() = 435111

    @Bindable
    fun getRepoName() = repo.name

    @Bindable
    fun getLanguage() = repo.language

    @Bindable
    fun isFavorite() = repo.favorite

    fun onFavouriteClick(view: View) {
        if (repo.favorite) {
            //store.dispatch(RemoveFavouriteAction(repo.name))
        } else {
            //store.dispatch(SaveFavouriteAction(repo.name))
        }
    }
}