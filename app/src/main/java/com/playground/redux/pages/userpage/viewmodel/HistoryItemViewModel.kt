package com.playground.redux.pages.userpage.viewmodel

import android.databinding.Bindable
import com.playground.redux.common.recyclerview.ListItemViewModel

class HistoryItemViewModel(private val text: String): ListItemViewModel() {

    @Bindable
    fun getText(): String = text

    override fun getViewType() = 436532

}