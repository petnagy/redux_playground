package com.playground.redux.pages.repospage.viewmodel

import android.databinding.Bindable
import android.view.View
import com.playground.redux.actions.RemoveFavouriteAction
import com.playground.redux.actions.RepoSelectedAction
import com.playground.redux.actions.SaveFavouriteAction
import com.playground.redux.appstate.AppState
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.data.GitHubRepo
import tw.geothings.rekotlin.Store

class RepoItemViewModel(private val repo: GitHubRepo, val store: Store<AppState>): ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.repo.name == (newItem as RepoItemViewModel).repo.name
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        (newItem as RepoItemViewModel)
        return this.repo.name == newItem.repo.name && this.repo.favorite == newItem.repo.favorite
    }

    override fun getViewType() = 435111

    @Bindable
    fun getRepoName() = repo.name

    @Bindable
    fun getLanguage() = repo.language

    @Bindable
    fun isFavorite() = repo.favorite

    fun onFavouriteClick(view: View) {
        if (repo.favorite) {
            store.dispatch(RemoveFavouriteAction(repo.owner.login, repo.name))
        } else {
            store.dispatch(SaveFavouriteAction(repo.owner.login, repo.name))
        }
    }

    fun onCardClicked(view: View) {
        store.dispatch(RepoSelectedAction(repo.name))
    }
}