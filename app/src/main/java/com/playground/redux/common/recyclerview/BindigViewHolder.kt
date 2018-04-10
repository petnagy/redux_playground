package com.playground.redux.common.recyclerview

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindigViewHolder: RecyclerView.ViewHolder {

    private val binding: ViewDataBinding

    constructor(binding: ViewDataBinding): super(binding.root) {
        this.binding = binding
    }

    fun getBinding(): ViewDataBinding {
        return binding
    }
}