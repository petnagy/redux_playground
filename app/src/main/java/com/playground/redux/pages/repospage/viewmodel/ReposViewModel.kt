package com.playground.redux.pages.repospage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.playground.redux.appstate.AppState
import timber.log.Timber
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber
import com.android.databinding.library.baseAdapters.BR

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
            Timber.d("Selected user: ${state.user.selectedUserName}")
            loading = state.repos.loading
            repoItems = state.repos.repoList.map { repo -> RepoItemViewModel(repo) }
            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.repoItems)
        }
    }

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName
}