package com.playground.redux.pages.repospage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.redux.appstate.AppState

class ReposViewModel(var store: Store<AppState>): BaseObservable(), StoreSubscriber<AppState> {

    @Bindable
    var repoItems: List<RepoItemViewModel> = emptyList()

    @Bindable
    var loading: Boolean = false

    fun onStart() {
        store.subscribe(this)
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        state.apply {
            this@ReposViewModel.loading = state.repos.loading
            repoItems = state.repos.repoList.map { repo -> RepoItemViewModel(repo, store) }
            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.repoItems)
        }
    }

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName
}