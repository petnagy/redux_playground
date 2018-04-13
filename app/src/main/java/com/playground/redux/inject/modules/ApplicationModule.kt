package com.playground.redux.inject.modules

import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.UserState
import com.playground.redux.middlewares.userMiddleware
import com.playground.redux.reducer.appReducer
import dagger.Module
import dagger.Provides
import tw.geothings.rekotlin.Store
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideStore(): Store<AppState> {
        return Store(reducer = ::appReducer, state = AppState(UserState()), middleware = listOf(userMiddleware))
    }

}