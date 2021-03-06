package com.playground.redux.redux.middlewares

import android.annotation.SuppressLint
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.playground.redux.data.UserSearch
import com.playground.redux.redux.actions.*
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.repository.Repository
import com.playground.redux.repository.usersearch.GetAllRecordsSpecification
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserMiddleware(private val userRepository: Repository<UserSearch>): Middleware<AppState> {
    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when (action) {
            is LoadPreviousSearchAction -> loadPreviousUserSearches(store, userRepository)
            is UserSelectionAction -> handleUserSelectionAction(store, userRepository, action)
            is PreviousSearchDeleteAction -> handlePreviousSearchDeleteAction(store, userRepository, action)
            is UndoUserSearchDeleteAction -> handleUndoDeleteAction(store, userRepository, action)
        }
        next.dispatch(action)
    }

    @SuppressLint("CheckResult")
    private fun loadPreviousUserSearches(store: Store<AppState>, userRepository: Repository<UserSearch>) {
        userRepository.query(GetAllRecordsSpecification())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> handleUserSearchesFromDb(store, result) },
                        { error -> handleUserSearchesError(store, error.message) }
                )
    }

    private fun handleUserSearchesFromDb(store: Store<AppState>, result: List<UserSearch>) {
        store.dispatch(PreviousSearchListAction(result))
    }

    private fun handleUserSearchesError(store: Store<AppState>, message: String?) {
        Timber.e(message)
        //TODO error handling
    }

    private fun handleUserSelectionAction(store: Store<AppState>, userRepository: Repository<UserSearch>, selectionAction: UserSelectionAction) {
        userRepository.add(UserSearch(selectionAction.selectedUser, System.currentTimeMillis()))
                .doOnComplete { store.dispatch(AddHistoryAction(selectionAction.selectedUser)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun handlePreviousSearchDeleteAction(store: Store<AppState>, userRepository: Repository<UserSearch>, action: PreviousSearchDeleteAction) {
        userRepository.remove(action.userSearch)
                .doOnComplete { store.dispatch(LoadPreviousSearchAction()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun handleUndoDeleteAction(store: Store<AppState>, userRepository: Repository<UserSearch>, action: UndoUserSearchDeleteAction) {
        userRepository.add(action.userSearch)
                .doOnComplete { store.dispatch(LoadPreviousSearchAction()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}