package com.playground.redux.inject.modules

import com.playground.redux.pages.userpage.MainActivity
import com.playground.redux.inject.PerActivity
import com.playground.redux.pages.repospage.ReposActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface DaggerActivityModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    fun contributeMainActivityInjector(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(ReposActivityModule::class)])
    fun contributeReposActivityInjector(): ReposActivity
}