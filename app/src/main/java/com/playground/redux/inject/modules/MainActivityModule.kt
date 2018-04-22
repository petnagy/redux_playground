package com.playground.redux.inject.modules

import com.playground.redux.redux.appstate.AppState
import com.playground.redux.pages.userpage.viewmodel.UserViewModel
import dagger.Module
import dagger.Provides
import tw.geothings.rekotlin.Store

@Module
class MainActivityModule {

    @Provides
    fun provideMainActivityViewModel(store: Store<AppState>) = UserViewModel(store)

}