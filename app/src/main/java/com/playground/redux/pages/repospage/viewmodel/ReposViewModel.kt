package com.playground.redux.pages.repospage.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.extensions.default
import com.playground.redux.redux.appstate.AppState

class ReposViewModel(var store: Store<AppState>): BaseObservable(), StoreSubscriber<AppState> {

    var repoItems = MutableLiveData<List<RepoItemViewModel>>().default(emptyList())
    var loading = MutableLiveData<Boolean>().default(false)

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName

    fun onStart() {
        store.subscribe(this)
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        state.repos.apply {
            this@ReposViewModel.loading.value = this.loading
            repoItems.value = this.repoList.map { repo -> RepoItemViewModel(repo, store) }
        }
    }
}