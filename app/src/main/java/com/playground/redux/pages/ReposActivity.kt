package com.playground.redux.pages

import android.os.Bundle
import com.playground.redux.actions.NextPageAction
import com.playground.redux.appstate.AppState
import com.playground.redux.navigation.Page
import dagger.android.support.DaggerAppCompatActivity
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber
import javax.inject.Inject

class ReposActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        store.dispatch(NextPageAction(Page.USER_SELECT_PAGE))
    }

    override fun newState(state: AppState) {
        state.apply {
            if (state.actualPage == Page.USER_SELECT_PAGE) {
                finish()
            }
        }
    }
}