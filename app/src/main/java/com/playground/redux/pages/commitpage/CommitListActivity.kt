package com.playground.redux.pages.commitpage

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.BR
import com.playground.redux.R
import com.playground.redux.databinding.ActivityCommitsBinding
import com.playground.redux.navigation.Page
import com.playground.redux.pages.commitpage.viewmodel.CommitListViewModel
import com.playground.redux.redux.actions.ClearCommitListAction
import com.playground.redux.redux.actions.LoadCommitsAction
import com.playground.redux.redux.actions.NextPageAction
import com.playground.redux.redux.appstate.AppState
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
        binding.setLifecycleOwner(this)
        lifecycle.addObserver(viewModel)
        store.dispatch(LoadCommitsAction(store.state.user.selectedUserName, store.state.repos.selectedRepoName))
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        store.dispatch(NextPageAction(Page.REPO_SELECT_PAGE))
    }

    override fun newState(state: AppState) {
        state.pageState.apply {
            if (this.actualPage != Page.COMMIT_LIST_PAGE) {
                store.unsubscribe(this@CommitListActivity)
                viewModel.onStop()
                store.dispatch(ClearCommitListAction())
                finish()
            }
        }
    }

}