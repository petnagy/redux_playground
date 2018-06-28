package com.playground.redux.pages.userpage.viewmodel

import android.view.View
import com.playground.redux.data.UserSearch

interface UserSearchCallback {

    fun onUserSearchClicked(userSearch: UserSearch)

    fun onUserSearchDelete(userSearch: UserSearch, view: View)

}