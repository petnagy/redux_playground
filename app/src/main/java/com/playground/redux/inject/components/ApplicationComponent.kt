package com.playground.redux.inject.components

import com.petnagy.koredux.Store
import com.playground.redux.ProjectApplication
import com.playground.redux.inject.modules.ApplicationModule
import com.playground.redux.inject.modules.DaggerActivityModule
import com.playground.redux.redux.appstate.AppState
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (ApplicationModule::class), (DaggerActivityModule::class)])
interface ApplicationComponent : AndroidInjector<ProjectApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ProjectApplication>()

    fun exposeStore(): Store<AppState>
}