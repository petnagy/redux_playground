package com.playground.redux.inject.modules

import com.playground.redux.appstate.AppState
import com.playground.redux.pages.githubuserpage.viewmodel.GitHubUserViewModel
import dagger.Module
import dagger.Provides
import tw.geothings.rekotlin.Store

@Module
class MainActivityModule {

    @Provides
    fun provideMainActivityViewModel(store: Store<AppState>) : GitHubUserViewModel = GitHubUserViewModel(store)

}