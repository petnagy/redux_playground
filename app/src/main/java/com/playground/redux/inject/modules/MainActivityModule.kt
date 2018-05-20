package com.playground.redux.inject.modules

import com.playground.redux.redux.appstate.AppState
import com.playground.redux.pages.userpage.viewmodel.UserViewModel
import com.playground.redux.redux_impl.Store
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideMainActivityViewModel(store: Store<AppState>) = UserViewModel(store)

}