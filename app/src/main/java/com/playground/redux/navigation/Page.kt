package com.playground.redux.navigation

import android.app.Activity
import com.playground.redux.pages.ReposActivity
import com.playground.redux.pages.userpage.MainActivity
import kotlin.reflect.KClass

enum class Page(val clazz: KClass<out Activity>) {
    USER_SELECT_PAGE(MainActivity::class),
    REPO_SELECT_PAGE(ReposActivity::class)
}