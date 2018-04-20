package com.playground.redux.pages.repospage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.RepoState
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber

class ReposViewModel(var store: Store<AppState>): BaseObservable(), StoreSubscriber<RepoState> {

    @Bindable
    var repoItems: List<RepoItemViewModel> = emptyList()

    @Bindable
    var loading: Boolean = false

    fun onStart() {
        store.subscribe(this) {
            it.select {
                it.repos
            }
        }
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: RepoState) {
        state.apply {
            this@ReposViewModel.loading = state.loading
            repoItems = state.repoList.map { repo -> RepoItemViewModel(repo, store) }
            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.repoItems)
        }
    }

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName
}