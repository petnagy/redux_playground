package com.playground.redux.common.recyclerview

import android.databinding.BaseObservable

abstract class ListItemViewModel: BaseObservable() {

    abstract fun getViewType(): Int

    abstract fun areItemsTheSame(newItem: ListItemViewModel): Boolean

    abstract fun areContentsTheSame(newItem: ListItemViewModel): Boolean
}