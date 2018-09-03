package com.playground.redux.inject.modules

import com.petnagy.koredux.Store
import com.playground.redux.pages.commitpage.viewmodel.CommitListViewModel
import com.playground.redux.redux.appstate.AppState
import dagger.Module
import dagger.Provides

@Module
class CommitPageModule {

    @Provides
    fun provideCommitPageViewModel(store: Store<AppState>) = CommitListViewModel(store)

}