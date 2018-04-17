package com.playground.redux.pages.userpage.viewmodel

import android.databinding.Bindable
import com.playground.redux.common.recyclerview.ListItemViewModel

class HistoryItemViewModel(private val text: String): ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.text == (newItem as HistoryItemViewModel).text
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.text == (newItem as HistoryItemViewModel).text
    }

    @Bindable
    fun getText(): String = text

    override fun getViewType() = 436532

}