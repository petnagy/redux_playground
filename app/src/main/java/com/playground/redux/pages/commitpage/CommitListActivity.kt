package com.playground.redux.pages.commitpage

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.playground.redux.BR
import com.playground.redux.R
import com.playground.redux.redux.actions.ClearCommitListAction
import com.playground.redux.redux.actions.LoadCommitsAction
import com.playground.redux.redux.actions.NextPageAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.databinding.ActivityCommitsBinding
import com.playground.redux.navigation.Page
import com.playground.redux.pages.commitpage.viewmodel.CommitListViewModel
import com.playground.redux.redux_impl.Store
import com.playground.redux.redux_impl.StoreSubscriber
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CommitListActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    @Inject
    lateinit var viewModel: CommitListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCommitsBinding = DataBindingUtil.setContentView(this, R.layout.activity_commits)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()

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
                store.unsubscribe(this@CommitListActivity)
                viewModel.onStop()
                store.dispatch(ClearCommitListAction())
                finish()
            }
        }
    }

}