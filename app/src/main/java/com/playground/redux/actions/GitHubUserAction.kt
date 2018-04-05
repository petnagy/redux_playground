package com.playground.redux.actions

import tw.geothings.rekotlin.Action

class SelectGitHubUserAction(val gitHubUser: String) : Action

class AddHistoryAction(val gitHubUser: String): Action