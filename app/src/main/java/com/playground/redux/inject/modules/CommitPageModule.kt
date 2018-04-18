package com.playground.redux.inject.modules

import com.playground.redux.appstate.AppState
import com.playground.redux.pages.commitoage.viewmodel.CommitListViewModel
import dagger.Module
import dagger.Provides
import tw.geothings.rekotlin.Store

@Module
class CommitPageModule {

    @Provides
    fun provideCommitPageViewModel(store: Store<AppState>) = CommitListViewModel(store)

}