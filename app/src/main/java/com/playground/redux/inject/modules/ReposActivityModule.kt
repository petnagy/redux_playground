package com.playground.redux.inject.modules

import com.petnagy.koredux.Store
import com.playground.redux.pages.repospage.viewmodel.ReposViewModel
import com.playground.redux.redux.appstate.AppState
import dagger.Module
import dagger.Provides

@Module
class ReposActivityModule {

    @Provides
    fun provideReposActivityViewModel(store: Store<AppState>) = ReposViewModel(store)

}