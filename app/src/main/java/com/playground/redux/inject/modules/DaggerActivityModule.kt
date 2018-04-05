package com.playground.redux.inject.modules

import com.playground.redux.pages.githubuserpage.MainActivity
import com.playground.redux.inject.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DaggerActivityModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    fun contributeMainActivityInjector(): MainActivity
}