package com.playground.redux.inject.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.petnagy.koredux.Store
import com.playground.redux.ProjectApplication
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.data.UserSearch
import com.playground.redux.inject.AppContext
import com.playground.redux.navigation.Navigator
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.appstate.*
import com.playground.redux.redux.middlewares.*
import com.playground.redux.redux.reducer.AppReducer
import com.playground.redux.repository.Repository
import com.playground.redux.repository.gitrepo.GitRepoRoomRepository
import com.playground.redux.repository.usersearch.UserSearchRoomRepository
import com.playground.redux.room.AppDatabase
import com.playground.redux.room.GitRepoDao
import com.playground.redux.room.UserSearchDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class  ApplicationModule {

    companion object {
        private const val TIME_OUT_IN_SEC: Long = 60
        private const val API_BASE_URL = "https://api.github.com/"
    }

    @Provides
    @AppContext
    internal fun provideContext(application: ProjectApplication): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideNavigator() = Navigator()

    @Singleton
    @Provides
    fun provideStore(navigator: Navigator, endpoint: GitHubEndpoint,
                     @Named("GIT_REPO") githubRepo: Repository<GitHubRepoEntity>,
                     @Named("USER_SEARCH") userSearchRepo: Repository<UserSearch>): Store<AppState> {
        return Store(reducer = AppReducer(), initState = AppState(UserState(), PageState(), RepoState(), CommitState()),
                middlewareList = listOf(LoggingMiddleware(),
                        UserMiddleware(userSearchRepo),
                        NavigationMiddleware(navigator),
                        RepoMiddleware(endpoint, githubRepo),
                        CommitMiddleware(endpoint)))
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
    fun provideUserSearch(appDatabase: AppDatabase): UserSearchDao {
        return appDatabase.userSearchDao()
    }

    @Provides
    @Singleton
    @Named("GIT_REPO")
    fun provideGitRepoRoomRepository(gitRepoDao: GitRepoDao): Repository<GitHubRepoEntity> = GitRepoRoomRepository(gitRepoDao)

    @Provides
    @Singleton
    @Named("USER_SEARCH")
    fun provideUserSearchRepository(userSearchDao: UserSearchDao): Repository<UserSearch> = UserSearchRoomRepository(userSearchDao)
}