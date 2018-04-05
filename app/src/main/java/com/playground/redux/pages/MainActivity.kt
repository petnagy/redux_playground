package com.playground.redux.pages

import android.os.Bundle
import com.playground.redux.R
import com.playground.redux.actions.ChangeGitHubUser
import com.playground.redux.appstate.AppState
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOk.setOnClickListener {
            store.dispatch(ChangeGitHubUser(editGithubUser.text.toString()))
        }
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
        }
    }
}
