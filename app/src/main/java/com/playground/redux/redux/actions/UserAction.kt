package com.playground.redux.redux.actions

import com.petnagy.koredux.Action
import com.playground.redux.data.UserSearch

class LoadPreviousSearchAction: Action

class PreviousSearchListAction(val prevUserSearches: List<UserSearch>): Action

class UserSelectionAction(val selectedUser: String) : Action

class AddHistoryAction(val selectedUser: String): Action

class UserTypeAction(val typedText: String): Action

class PreviousSearchDeleteAction(val userSearch: UserSearch): Action

class UndoUserSearchDeleteAction(val userSearch: UserSearch): Action