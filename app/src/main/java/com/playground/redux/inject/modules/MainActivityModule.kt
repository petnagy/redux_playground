package com.playground.redux.inject.modules

import com.petnagy.koredux.Store
import com.playground.redux.pages.userpage.viewmodel.UserViewModel
import com.playground.redux.redux.appstate.AppState
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideMainActivityViewModel(store: Store<AppState>) = UserViewModel(store)

}