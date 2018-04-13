package com.playground.redux.pages.userpage

import android.support.v7.util.DiffUtil

class HistoryDiffCallback(private val oldHistoryItems: List<String>, private val newHistoryItems: List<String>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldHistoryItems.size
    }

    override fun getNewListSize(): Int {
        return newHistoryItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryItems[oldItemPosition] == newHistoryItems[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryItems[oldItemPosition] == newHistoryItems[newItemPosition]
    }

}