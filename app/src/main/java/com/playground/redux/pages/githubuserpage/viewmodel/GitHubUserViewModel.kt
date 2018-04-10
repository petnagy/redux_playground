package com.playground.redux.pages.githubuserpage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.playground.redux.actions.SelectGitHubUserAction
import com.playground.redux.actions.UserTypeAction
import com.playground.redux.appstate.AppState
import timber.log.Timber
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber

class GitHubUserViewModel(var store: Store<AppState>): BaseObservable(), StoreSubscriber<AppState> {

    var gitHubUser: String = ""

    @Bindable
    var historyItems: List<HistoryItemViewModel> = emptyList()

    fun onOkButtonClicked(view: View) {
        Timber.d("Ok Button pressed")
        store.dispatch(SelectGitHubUserAction(gitHubUser))
    }

    @Bindable
    fun getGithubUserWatcher(): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    store.dispatch(UserTypeAction(it.toString()))
                }
            }
        }
    }

    fun onStart() {
        store.subscribe(this)
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: AppState) {
        state.apply {
            Timber.d("Selected github user: ${state.githubUser.selectedUserName}")
            state.githubUser.history.let {
                historyItems = state.githubUser.history
                        .filter { typedUserName -> typedUserName.startsWith(state.githubUser.typedName) }
                        .map { typedUserName -> HistoryItemViewModel(typedUserName) }
                        .toMutableList()
                notifyPropertyChanged(BR.historyItems)
            }
        }
    }
}