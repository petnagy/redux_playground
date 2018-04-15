package com.playground.redux.pages.repospage

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.playground.redux.R
import com.playground.redux.BR
import com.playground.redux.actions.LoadReposAction
import com.playground.redux.actions.NextPageAction
import com.playground.redux.appstate.AppState
import com.playground.redux.databinding.ActivityReposBinding
import com.playground.redux.navigation.Page
import com.playground.redux.pages.repospage.viewmodel.ReposViewModel
import dagger.android.support.DaggerAppCompatActivity
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber
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
        state.apply {
            if (state.actualPage == Page.USER_SELECT_PAGE) {
                store.unsubscribe(this@ReposActivity)
                viewModel.onStop()
                finish()
            }
        }
    }
}