package com.playground.redux.pages.commitpage.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.extensions.default
import com.playground.redux.redux.appstate.AppState

class CommitListViewModel(val store: Store<AppState>): BaseObservable(), StoreSubscriber<AppState> {

    val commitItems = MutableLiveData<List<CommitItemViewModel>>().default(emptyList())
    var loading = MutableLiveData<Boolean>().default(false)

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName

    @Bindable
    fun getRepo(): String = store.state.repos.selectedRepoName

    fun onStart() {
        store.subscribe(this)
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        state.commits.apply {
            this@CommitListViewModel.loading.value = this.loading
            commitItems.value = this.commitList.map { commit -> CommitItemViewModel(commit) }
        }
    }

}