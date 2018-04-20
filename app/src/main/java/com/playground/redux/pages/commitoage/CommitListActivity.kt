package com.playground.redux.pages.commitoage

import android.os.Bundle
import com.playground.redux.actions.ClearCommitListAction
import com.playground.redux.actions.LoadCommitsAction
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

        store.dispatch(LoadCommitsAction(store.state.user.selectedUserName, store.state.repos.selectedRepoName))
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this)
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
        viewModel.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        store.dispatch(NextPageAction(Page.REPO_SELECT_PAGE))
    }

    override fun newState(state: AppState) {
        state.apply {
            if (state.pageState.actualPage != Page.COMMIT_LIST_PAGE) {
//                store.unsubscribe(this@CommitListActivity)
//                viewModel.onStop()
                store.dispatch(ClearCommitListAction())
                finish()
            }
        }
    }

}