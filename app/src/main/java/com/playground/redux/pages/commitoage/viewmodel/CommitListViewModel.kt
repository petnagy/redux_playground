package com.playground.redux.pages.commitoage.viewmodel

import com.playground.redux.appstate.AppState
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber

class CommitListViewModel(val store: Store<AppState>): StoreSubscriber<AppState> {

    fun onStart() {
        store.subscribe(this)
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {

    }

}