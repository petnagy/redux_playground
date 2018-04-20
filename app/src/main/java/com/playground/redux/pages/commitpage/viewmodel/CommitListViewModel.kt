package com.playground.redux.pages.commitpage.viewmodel

import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.CommitState
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber

class CommitListViewModel(val store: Store<AppState>): StoreSubscriber<CommitState> {

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

    }

}