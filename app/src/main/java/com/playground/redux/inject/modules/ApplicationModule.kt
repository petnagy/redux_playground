package com.playground.redux.inject.modules

import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.UserState
import com.playground.redux.middlewares.navigationMiddleware
import com.playground.redux.middlewares.userMiddleware
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.reducer.appReducer
import dagger.Module
import dagger.Provides
import tw.geothings.rekotlin.Store
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideNavigator() = Navigator()

    @Singleton
    @Provides
    fun provideStore(navigator: Navigator): Store<AppState> {
        return Store(reducer = ::appReducer, state = AppState(UserState(), Page.USER_SELECT_PAGE), middleware = listOf(userMiddleware, navigationMiddleware(navigator)))
    }

}