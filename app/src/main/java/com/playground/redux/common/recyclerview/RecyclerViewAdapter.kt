package com.playground.redux.common.recyclerview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.playground.redux.BR

class RecyclerViewAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    var items: MutableList<ListItemViewModel> = ArrayList()
        set(value) {
            oldItems = ArrayList(items)
            this.items.clear()
            this.items.addAll(value)
            var diffResult = DiffUtil.calculateDiff(ListItemViewModelDiffCallback(oldItems, items))
            diffResult.dispatchUpdatesTo(this)
        }

    var itemLayout: Int = 0

    private var oldItems: ArrayList<ListItemViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        var dataBinding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), itemLayout, parent, false)
        return BindingViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.getBinding().setVariable(BR.viewModel, items[position])
        holder.getBinding().executePendingBindings()
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        }
    }
}