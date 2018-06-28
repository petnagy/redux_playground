package com.playground.redux.redux.middlewares

import com.playground.redux.data.UserSearch
import com.playground.redux.redux.actions.*
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
        is UserSelectionAction -> handleUserSelectionAction(store, userRepository, action)
        is PreviousSearchDeleteAction -> handlePreviousSearchDeleteAction(store, userRepository, action)
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

fun handleUserSelectionAction(store: Store<AppState>, userRepository: Repository<UserSearch>, selectionAction: UserSelectionAction) {
    store.dispatch(AddHistoryAction(selectionAction.selectedUser))
    userRepository.add(UserSearch(selectionAction.selectedUser, System.currentTimeMillis()))
}

fun handlePreviousSearchDeleteAction(store: Store<AppState>, userRepository: Repository<UserSearch>, action: PreviousSearchDeleteAction) {
    userRepository.remove(action.userSearch)
    store.dispatch(LoadPreviousSearchAction())
}