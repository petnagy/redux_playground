package com.playground.redux.pages.commitoage

import android.os.Bundle
import com.playground.redux.actions.NextPageAction
import com.playground.redux.appstate.AppState
import com.playground.redux.navigation.Page
import com.playground.redux.pages.commitoage.viewmodel.CommitListViewModel
import dagger.android.support.DaggerAppCompatActivity
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber
import javax.inject.Inject

class CommitListActivity: DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    @Inject
    lateinit var viewModel: CommitListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        store.dispatch(NextPageAction(Page.REPO_SELECT_PAGE))
    }

    override fun newState(state: AppState) {
        state.apply {
            if (state.actualPage != Page.COMMIT_LIST_PAGE) {
                store.unsubscribe(this@CommitListActivity)
                viewModel.onStop()
                finish()
            }
        }
    }

}