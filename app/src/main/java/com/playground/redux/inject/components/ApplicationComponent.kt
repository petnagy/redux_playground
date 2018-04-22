package com.playground.redux.inject.components

import com.playground.redux.ProjectApplication
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.inject.modules.ApplicationModule
import com.playground.redux.inject.modules.DaggerActivityModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import tw.geothings.rekotlin.Store
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (ApplicationModule::class), (DaggerActivityModule::class)])
interface ApplicationComponent : AndroidInjector<ProjectApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ProjectApplication>()

    fun exposeStore(): Store<AppState>
}