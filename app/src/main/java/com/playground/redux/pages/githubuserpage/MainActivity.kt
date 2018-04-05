package com.playground.redux.pages.githubuserpage

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.playground.redux.R
import com.playground.redux.actions.SelectGitHubUserAction
import com.playground.redux.appstate.AppState
import com.playground.redux.common.SpaceItemDecorator
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    private var historyAdapter: GitHubUserHistoryAdapter = GitHubUserHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOk.setOnClickListener {
            store.dispatch(SelectGitHubUserAction(editGithubUser.text.toString()))
        }
        githubHistoryList.layoutManager = GridLayoutManager(this, 1)
        githubHistoryList.addItemDecoration(SpaceItemDecorator(8))
        githubHistoryList.adapter = historyAdapter
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        state.apply {
            Timber.d("GitHubUser: ${state.githubUser?.selectedUserName}")
            if (state.githubUser?.history != null) {
                historyAdapter.searchedItems = state.githubUser.history
                historyAdapter.notifyDataSetChanged()
            }
        }
    }
}
