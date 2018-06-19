package com.playground.redux.redux.actions

import com.playground.redux.data.UserSearch
import com.playground.redux.redux_impl.Action

class LoadPreviousSearchAction: Action

class PreviousSearchListAction(val prevUserSearches: List<UserSearch>): Action

class SelectUserAction(val selectedUser: String) : Action

class AddHistoryAction(val selectedUser: String): Action

class UserTypeAction(val typedText: String): Action

class HistoryItemDeleteAction(val userName: String): Action

class SwipeToDeleteAction(val position: Int): Action