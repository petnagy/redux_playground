package com.playground.redux.appstate

import com.playground.redux.navigation.Page
import tw.geothings.rekotlin.StateType

data class PageState(val actualPage: Page = Page.USER_SELECT_PAGE): StateType