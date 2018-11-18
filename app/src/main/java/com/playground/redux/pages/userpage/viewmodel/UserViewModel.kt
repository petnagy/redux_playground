package com.playground.redux.pages.userpage.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.data.UserSearch
import com.playground.redux.extensions.default
import com.playground.redux.redux.actions.PreviousSearchDeleteAction
import com.playground.redux.redux.actions.UndoUserSearchDeleteAction
import com.playground.redux.redux.actions.UserSelectionAction
import com.playground.redux.redux.actions.UserTypeAction
import com.playground.redux.redux.appstate.AppState
import timber.log.Timber

class UserViewModel(var store: Store<AppState>) : BaseObservable(), StoreSubscriber<AppState>, UserSearchCallback, LifecycleObserver {

    var user: String = store.state.user.typedName

    var loading = MutableLiveData<Boolean>().default(false)
    var historyItems = MutableLiveData<List<HistoryItemViewModel>>().default(emptyList())

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        store.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        store.unsubscribe(this)
    }

    @Bindable
    fun getUserWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    store.dispatch(UserTypeAction(it.toString()))
                }
            }
        }
    }

    @Bindable
    fun getSwipeToDeleteCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val userSearchList = store.state.user.history.filter { userSearch -> userSearch.userName.startsWith(store.state.user.typedName) }
                val deletingUserSearch = userSearchList[viewHolder.adapterPosition]
                store.dispatch(PreviousSearchDeleteAction(deletingUserSearch))
                showUndoSnackbar(viewHolder.itemView, deletingUserSearch)
            }
        }
    }

    fun onOkButtonClicked(view: View) {
        Timber.d("Ok Button pressed")
        if (user.isNotBlank()) {
            store.dispatch(UserSelectionAction(user))
        }
    }

    fun showUndoSnackbar(view: View, userSearch: UserSearch) {
        Snackbar.make(view, "User name will be delete: ${userSearch.userName}", Snackbar.LENGTH_LONG)
                .setAction("UNDO") { store.dispatch(UndoUserSearchDeleteAction(userSearch)) }.show()
    }

    override fun onUserSearchClicked(userSearch: UserSearch) {
        store.dispatch(UserSelectionAction(userSearch.userName))
    }

    override fun onUserSearchDelete(userSearch: UserSearch, view: View) {
        store.dispatch(PreviousSearchDeleteAction(userSearch))
        showUndoSnackbar(view, userSearch)
    }

    override fun newState(state: AppState) {
        state.user.apply {
            this@UserViewModel.loading.value = this.loading
            this.history.let { historyList ->
                Timber.d("History item list size: ${historyList.size}")
                historyItems.value = historyList
                        .asSequence()
                        .filter { userSearch -> userSearch.userName.startsWith(this.typedName) }
                        .map { userSearch -> HistoryItemViewModel(userSearch, this@UserViewModel) }
                        .toMutableList()
            }
        }
    }
}