package com.playground.redux.pages.userpage.viewmodel

import android.databinding.Bindable
import android.view.View
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.redux.actions.PreviousSearchDeleteAction
import com.playground.redux.redux.actions.UserSelectionAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Store
import timber.log.Timber

class HistoryItemViewModel(private val index: Int, private val text: String, private val store: Store<AppState>): ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.text == (newItem as HistoryItemViewModel).text
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.text == (newItem as HistoryItemViewModel).text
    }

    @Bindable
    fun getText(): String = text

    override fun getViewType() = 436532

    fun onHistoryItemClicked(view: View) {
        Timber.d("HistoryItemClicked: $text")
        store.dispatch(UserSelectionAction(text))
    }

    fun onHistoryItemDeleteClicked(view: View) {
        Timber.d("HistoryItemDeleteClicked: $text")
        store.dispatch(PreviousSearchDeleteAction(index))
    }

}