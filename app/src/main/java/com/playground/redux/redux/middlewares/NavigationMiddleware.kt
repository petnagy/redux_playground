package com.playground.redux.redux.middlewares

import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.redux.actions.NextPageAction
import com.playground.redux.redux.actions.RepoSelectedAction
import com.playground.redux.redux.actions.UserSelectionAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Action
import com.playground.redux.redux_impl.DispatchFunction
import com.playground.redux.redux_impl.Middleware
import com.playground.redux.redux_impl.Store

class NavigationMiddleware(private val navigator: Navigator): Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is UserSelectionAction -> store.dispatch(NextPageAction(navigator.goNextPage(Page.USER_SELECT_PAGE)))
            is RepoSelectedAction -> store.dispatch(NextPageAction(navigator.goNextPage(Page.REPO_SELECT_PAGE)))
        }
        next.dispatch(action)
    }

}