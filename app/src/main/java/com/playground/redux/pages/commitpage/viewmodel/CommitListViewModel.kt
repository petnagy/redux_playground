package com.playground.redux.pages.commitpage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.redux.appstate.AppState

class CommitListViewModel(val store: Store<AppState>): BaseObservable(), StoreSubscriber<AppState> {

    @Bindable
    var commitItems: List<CommitItemViewModel> = emptyList()

    @Bindable
    var loading: Boolean = false

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
            this@CommitListViewModel.loading = this.loading
            commitItems = this.commitList.map { commit -> CommitItemViewModel(commit) }
            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.commitItems)
        }
    }

}