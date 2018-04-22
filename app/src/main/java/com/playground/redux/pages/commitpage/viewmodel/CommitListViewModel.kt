package com.playground.redux.pages.commitpage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux.appstate.CommitState
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber

class CommitListViewModel(val store: Store<AppState>): BaseObservable(), StoreSubscriber<CommitState> {

    @Bindable
    var commitItems: List<CommitItemViewModel> = emptyList()

    @Bindable
    var loading: Boolean = false

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName

    @Bindable
    fun getRepo(): String = store.state.repos.selectedRepoName

    fun onStart() {
        store.subscribe(this) {
            it.select {
                it.commits
            }
        }
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: CommitState) {
        state.apply {
            this@CommitListViewModel.loading = state.loading
            commitItems = state.commitList.map { commit -> CommitItemViewModel(commit) }
            notifyPropertyChanged(BR.loading)
            notifyPropertyChanged(BR.commitItems)
        }
    }

}