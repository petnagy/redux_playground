package com.playground.redux.redux.middlewares

import com.playground.redux.data.UserSearch
import com.playground.redux.redux.actions.AddHistoryAction
import com.playground.redux.redux.actions.LoadPreviousSearchAction
import com.playground.redux.redux.actions.PreviousSearchListAction
import com.playground.redux.redux.actions.SelectUserAction
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.redux_impl.Middleware
import com.playground.redux.redux_impl.Store
import com.playground.redux.repository.Repository
import com.playground.redux.repository.usersearch.GetAllRecordsSpecification
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

fun userMiddleware(@Named("USER_SEARCH") userRepository: Repository<UserSearch>): Middleware<AppState> = { store, action, next ->
    when (action) {
        is LoadPreviousSearchAction -> loadPreviousUserSearches(store, userRepository)
        is SelectUserAction -> handleUserSelectionAction(store, userRepository, action)
    }
    next.dispatch(action)
}

fun loadPreviousUserSearches(store: Store<AppState>, userRepository: Repository<UserSearch>) {
    userRepository.query(GetAllRecordsSpecification())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result -> handleUserSearchesFromDb(store, result) },
                    { error -> handleUserSearchesError(store) }
            )
}

fun handleUserSearchesFromDb(store: Store<AppState>, result: List<UserSearch>) {
    store.dispatch(PreviousSearchListAction(result))
}

fun handleUserSearchesError(store: Store<AppState>) {
    //TODO error handling
}

fun handleUserSelectionAction(store: Store<AppState>, userRepository: Repository<UserSearch>, action: SelectUserAction) {
    store.dispatch(AddHistoryAction(action.selectedUser))
    userRepository.add(UserSearch(action.selectedUser, System.currentTimeMillis()))
}
