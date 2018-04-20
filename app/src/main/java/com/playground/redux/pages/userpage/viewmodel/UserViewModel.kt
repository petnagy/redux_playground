package com.playground.redux.pages.userpage.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.playground.redux.actions.SelectUserAction
import com.playground.redux.actions.UserTypeAction
import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.UserState
import timber.log.Timber
import tw.geothings.rekotlin.Store
import tw.geothings.rekotlin.StoreSubscriber

class UserViewModel(var store: Store<AppState>): BaseObservable(), StoreSubscriber<UserState> {

    var user: String = store.state.user.selectedUserName

    @Bindable
    var historyItems: List<HistoryItemViewModel> = emptyList()

    fun onOkButtonClicked(view: View) {
        Timber.d("Ok Button pressed")
        if (user.isNotBlank()) {
            store.dispatch(SelectUserAction(user))
        }
    }

    @Bindable
    fun getUserWatcher(): TextWatcher {
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
        store.subscribe(this) {
            it.select {
                it.user
            }
        }
    }

    fun onStop() {
        store.unsubscribe(this)
    }

    override fun newState(state: UserState) {
        state.apply {
            state.history.let {
                historyItems = state.history
                        .filter { typedUserName -> typedUserName.startsWith(state.typedName) }
                        .map { typedUserName -> HistoryItemViewModel(typedUserName) }
                        .toMutableList()
                notifyPropertyChanged(BR.historyItems)
            }
        }
    }
}