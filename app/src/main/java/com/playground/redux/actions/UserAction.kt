package com.playground.redux.actions

import tw.geothings.rekotlin.Action

class SelectUserAction(val selectedUser: String) : Action

class AddHistoryAction(val selectedUser: String): Action

class UserTypeAction(val typedText: String): Action