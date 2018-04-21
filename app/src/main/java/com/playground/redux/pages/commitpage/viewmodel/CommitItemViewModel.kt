package com.playground.redux.pages.commitpage.viewmodel

import android.databinding.Bindable
import com.playground.redux.common.recyclerview.ListItemViewModel
import com.playground.redux.data.GitCommit

class CommitItemViewModel(private val commit: GitCommit) : ListItemViewModel() {

    override fun areItemsTheSame(newItem: ListItemViewModel): Boolean {
        return this.commit.commit.message == (newItem as CommitItemViewModel).commit.commit.message
    }

    override fun areContentsTheSame(newItem: ListItemViewModel): Boolean {
        return this.commit.commit.message == (newItem as CommitItemViewModel).commit.commit.message
    }

    override fun getViewType() = 541235

    @Bindable
    fun getCommitterName() = commit.commit.committer.name

    @Bindable
    fun getMessage() = commit.commit.message

    @Bindable
    fun getDate() = commit.commit.committer.date
}