package com.playground.redux.inject.modules

import com.playground.redux.redux.appstate.AppState
import com.playground.redux.pages.repospage.viewmodel.ReposViewModel
import dagger.Module
import dagger.Provides
import tw.geothings.rekotlin.Store

@Module
class ReposActivityModule {

    @Provides
    fun provideReposActivityViewModel(store: Store<AppState>) = ReposViewModel(store)

}