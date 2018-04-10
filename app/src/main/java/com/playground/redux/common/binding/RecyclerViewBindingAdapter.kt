package com.playground.redux.common.binding

import android.databinding.BindingAdapter
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.playground.redux.common.recyclerview.SpaceItemDecorator
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.common.recyclerview.RecyclerViewAdapter

@BindingAdapter("source")
fun setupRecyclerViewSource(recyclerView: RecyclerView, source: List<ListItemViewModel>) {
    getAdapter(recyclerView).items = source.toMutableList()
}

@BindingAdapter("itemLayout")
fun setupRecyclerViewItemLayout(recyclerView: RecyclerView, @LayoutRes itemLayout: Int) {
    getAdapter(recyclerView).itemLayout = itemLayout
}

private fun getAdapter(recyclerView: RecyclerView): RecyclerViewAdapter {
    var adapter: RecyclerViewAdapter? = recyclerView.adapter as? RecyclerViewAdapter
    if (adapter == null) {
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 1)
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecorator(8))
    }
    return adapter
}