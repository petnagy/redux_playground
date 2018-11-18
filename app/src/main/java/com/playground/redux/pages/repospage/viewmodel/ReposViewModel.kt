package com.playground.redux.pages.repospage.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.extensions.default
import com.playground.redux.redux.appstate.AppState

class ReposViewModel(var store: Store<AppState>): BaseObservable(), StoreSubscriber<AppState>, LifecycleObserver {

    var repoItems = MutableLiveData<List<RepoItemViewModel>>().default(emptyList())
    var loading = MutableLiveData<Boolean>().default(false)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        store.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        store.unsubscribe(this)
    }

    @Bindable
    fun getUser(): String = store.state.user.selectedUserName

    override fun newState(state: AppState) {
        state.repos.apply {
            this@ReposViewModel.loading.postValue(this.loading)
            repoItems.postValue(this.repoList.map { repo -> RepoItemViewModel(repo, store) })
        }
    }
}