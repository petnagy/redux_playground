package com.playground.redux.inject.modules

import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.GithubUserState
import com.playground.redux.middlewares.githubUserMiddleware
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
        return Store(reducer = ::appReducer, state = AppState(GithubUserState()), middleware = listOf(githubUserMiddleware))
    }

}