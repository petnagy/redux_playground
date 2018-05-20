package com.playground.redux.inject.modules

import com.playground.redux.redux.appstate.AppState
import com.playground.redux.pages.commitpage.viewmodel.CommitListViewModel
import com.playground.redux.redux_impl.Store
import dagger.Module
import dagger.Provides

@Module
class CommitPageModule {

    @Provides
    fun provideCommitPageViewModel(store: Store<AppState>) = CommitListViewModel(store)

}