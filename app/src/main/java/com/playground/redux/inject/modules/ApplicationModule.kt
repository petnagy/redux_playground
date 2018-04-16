package com.playground.redux.inject.modules

import com.google.gson.GsonBuilder
import com.playground.redux.appstate.AppState
import com.playground.redux.appstate.RepoState
import com.playground.redux.appstate.UserState
import com.playground.redux.middlewares.navigationMiddleware
import com.playground.redux.middlewares.reposMiddleware
import com.playground.redux.middlewares.userMiddleware
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.reducer.appReducer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import tw.geothings.rekotlin.Store
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.Gson
import com.playground.redux.network.GitHubEndpoint


@Module
class ApplicationModule {

    companion object {
        private const val TIME_OUT_IN_SEC: Long = 60
        private const val API_BASE_URL = "https://api.github.com/"
    }

    @Singleton
    @Provides
    fun provideNavigator() = Navigator()

    @Singleton
    @Provides
    fun provideStore(navigator: Navigator, endpoint: GitHubEndpoint): Store<AppState> {
        return Store(reducer = ::appReducer, state = AppState(UserState(), Page.USER_SELECT_PAGE, RepoState()),
                middleware = listOf(userMiddleware, navigationMiddleware(navigator), reposMiddleware(endpoint)))
    }

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
                .serializeNulls()
                .create()

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
                .readTimeout(TIME_OUT_IN_SEC, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT_IN_SEC, TimeUnit.SECONDS)
                .build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient) = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

    @Provides
    @Singleton
    fun provideEndpoint(retrofit: Retrofit) = retrofit.create<GitHubEndpoint>(GitHubEndpoint::class.java)

}