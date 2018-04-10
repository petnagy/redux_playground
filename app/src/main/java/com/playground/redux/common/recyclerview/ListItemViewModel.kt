package com.playground.redux.common.recyclerview

import android.databinding.BaseObservable

abstract class ListItemViewModel: BaseObservable() {

    abstract fun getViewType(): Int

    fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this == newItem
    }

    fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this == newItem
    }
}