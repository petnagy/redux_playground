package com.playground.redux.pages.githubuserpage

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.playground.redux.R

class GitHubUserHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var oldSearchedItems: ArrayList<String> = ArrayList()
    var searchedItems: MutableList<String> = ArrayList()
    set(value) {
        oldSearchedItems = ArrayList(searchedItems)
        this.searchedItems.clear()
        this.searchedItems.addAll(value)
        val diffResult = DiffUtil.calculateDiff(HistoryDiffCallback(oldSearchedItems, searchedItems))
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as HistoryItemViewHolder
        item.label.text = searchedItems[position]
    }

    override fun getItemCount(): Int {
        return searchedItems.size
    }
}

class HistoryItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    internal var label: TextView = view.findViewById(R.id.historyText)

}