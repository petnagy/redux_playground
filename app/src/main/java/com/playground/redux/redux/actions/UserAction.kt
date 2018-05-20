package com.playground.redux.redux.actions

import com.playground.redux.redux_impl.Action

class SelectUserAction(val selectedUser: String) : Action

class AddHistoryAction(val selectedUser: String): Action

class UserTypeAction(val typedText: String): Action