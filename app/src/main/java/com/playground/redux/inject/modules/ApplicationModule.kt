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
import com.playground.redux.middlewares.loggingMiddleware
import com.playground.redux.network.GitHubEndpoint
import android.arch.persistence.room.Room
import android.content.Context
import com.playground.redux.inject.AppContext
import com.playground.redux.repository.Repository
import com.playground.redux.repository.gitrepo.GitRepoEntity
import com.playground.redux.repository.gitrepo.GitRepoRoomRepository
import com.playground.redux.room.AppDatabase
import com.playground.redux.room.GitRepoDao
import javax.inject.Named


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
                middleware = listOf(loggingMiddleware, userMiddleware, navigationMiddleware(navigator), reposMiddleware(endpoint)))
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
                .serializeNulls()
                .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
                .readTimeout(TIME_OUT_IN_SEC, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT_IN_SEC, TimeUnit.SECONDS)
                .build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

    @Provides
    @Singleton
    fun provideEndpoint(retrofit: Retrofit): GitHubEndpoint = retrofit.create<GitHubEndpoint>(GitHubEndpoint::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@AppContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
    }

    @Provides
    @Singleton
    fun provideUserContent(appDatabase: AppDatabase): GitRepoDao {
        return appDatabase.gitRepoDao()
    }

    @Provides
    @Singleton
    @Named("GIT_REPO")
    fun provideGitRepoRoomRepository(gitRepoDao: GitRepoDao): Repository<GitRepoEntity> = GitRepoRoomRepository(gitRepoDao)
}