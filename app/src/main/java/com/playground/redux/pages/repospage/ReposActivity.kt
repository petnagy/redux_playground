package com.playground.redux.pages.repospage

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.BR
import com.playground.redux.R
import com.playground.redux.databinding.ActivityReposBinding
import com.playground.redux.navigation.Page
import com.playground.redux.pages.repospage.viewmodel.ReposViewModel
import com.playground.redux.redux.actions.ClearRepoItemsAction
import com.playground.redux.redux.actions.LoadReposAction
import com.playground.redux.redux.actions.NextPageAction
import com.playground.redux.redux.appstate.AppState
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ReposActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    @Inject
    lateinit var viewModel: ReposViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityReposBinding = DataBindingUtil.setContentView(this, R.layout.activity_repos)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()

        store.dispatch(LoadReposAction(store.state.user.selectedUserName))
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
        store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
        store.unsubscribe(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        store.dispatch(NextPageAction(Page.USER_SELECT_PAGE))
    }

    override fun newState(state: AppState) {
        state.pageState.apply {
            if (this.actualPage == Page.USER_SELECT_PAGE) {
                store.unsubscribe(this@ReposActivity)
                viewModel.onStop()
                store.dispatch(ClearRepoItemsAction())
                finish()
            } else if (this.actualPage == Page.COMMIT_LIST_PAGE) {
                store.unsubscribe(this@ReposActivity)
                viewModel.onStop()
                startActivity(Intent(this@ReposActivity, this.actualPage.clazz.java))
            }
        }
    }
}