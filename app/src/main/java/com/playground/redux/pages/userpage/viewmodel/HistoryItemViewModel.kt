package com.playground.redux.pages.userpage.viewmodel

import android.databinding.Bindable
import android.view.View
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.data.UserSearch
import timber.log.Timber

class HistoryItemViewModel(private val userSearch: UserSearch, private val callback: UserSearchCallback): ListItemViewModel() {

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
        callback.onUserSearchClicked(userSearch)
    }

    fun onHistoryItemDeleteClicked(view: View) {
        Timber.d("HistoryItemDeleteClicked: $userSearch.userName")
        callback.onUserSearchDelete(userSearch, view)
    }

}