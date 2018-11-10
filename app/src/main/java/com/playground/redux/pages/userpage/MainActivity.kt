package com.playground.redux.pages.userpage

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.playground.redux.BR
import com.playground.redux.R
import com.playground.redux.databinding.ActivityMainBinding
import com.playground.redux.navigation.Page
import com.playground.redux.pages.userpage.viewmodel.UserViewModel
import com.playground.redux.redux.actions.LoadPreviousSearchAction
import com.playground.redux.redux.appstate.AppState
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    @Inject
    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()

        store.dispatch(LoadPreviousSearchAction())
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

    override fun newState(state: AppState) {
        state.pageState.apply {
            if (this.actualPage != Page.USER_SELECT_PAGE) {
                startActivity(Intent(this@MainActivity, this.actualPage.clazz.java))
                store.unsubscribe(this@MainActivity)
                viewModel.onStop()
            }
        }
    }
}
