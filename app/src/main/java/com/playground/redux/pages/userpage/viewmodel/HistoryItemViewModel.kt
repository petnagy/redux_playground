package com.playground.redux.pages.userpage.viewmodel

import android.databinding.Bindable
import android.view.View
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.data.UserSearch
import com.playground.redux.redux.actions.PreviousSearchDeleteAction
import com.playground.redux.redux.actions.UserSelectionAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Store
import timber.log.Timber

class HistoryItemViewModel(private val userSearch: UserSearch, private val store: Store<AppState>): ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.userSearch.userName == (newItem as HistoryItemViewModel).userSearch.userName
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.userSearch.userName == (newItem as HistoryItemViewModel).userSearch.userName
    }

    @Bindable
    fun getText(): String = userSearch.userName

    override fun getViewType() = 436532

    fun onHistoryItemClicked(view: View) {
        Timber.d("HistoryItemClicked: $userSearch.userName")
        store.dispatch(UserSelectionAction(userSearch.userName))
    }

    fun onHistoryItemDeleteClicked(view: View) {
        Timber.d("HistoryItemDeleteClicked: $userSearch.userName")
        store.dispatch(PreviousSearchDeleteAction(userSearch))
    }

}