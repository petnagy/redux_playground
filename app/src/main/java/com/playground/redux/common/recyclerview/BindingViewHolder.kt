package com.playground.redux.common.recyclerview

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): ViewDataBinding {
        return binding
    }
}