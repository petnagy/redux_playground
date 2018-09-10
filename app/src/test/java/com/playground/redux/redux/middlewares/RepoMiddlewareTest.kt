package com.playground.redux.redux.middlewares

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.playground.redux.data.GitHubOwner
import com.playground.redux.data.GitHubRepo
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.actions.*
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.repository.Repository
import com.playground.redux.repository.gitrepo.GitRepoSpecification
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mockito
import java.io.IOException

class RepoMiddlewareTest {

    private lateinit var underTest: RepoMiddleware

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()

        private const val USER_NAME = "userName"
        private const val REPO_NAME = "repoName"
    }

    @Test
    fun testRepoMiddlewareLoadReposAction_SuccessLoading() {
        //GIVEN
        val mockEndpoint: GitHubEndpoint = mock()
        val mockRepository: Repository<GitHubRepoEntity> = mock()
        underTest = RepoMiddleware(mockEndpoint, mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val action = LoadReposAction(USER_NAME)
        val mockDispatchFunction: DispatchFunction = mock()
        val gitHubRepo = GitHubRepo(123, USER_NAME, "html_url", "language", GitHubOwner(USER_NAME, 123456789), false)
        val listOfGitHubRepo = listOf(gitHubRepo)
        Mockito.`when`(mockEndpoint.getRepos(USER_NAME)).thenReturn(Single.just(listOfGitHubRepo))
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(2)).dispatch(capture())
            Assert.assertTrue(firstValue is GitHubReposSuccessAction)
            val firstValuesArgument = (firstValue as GitHubReposSuccessAction).repoList
            Assert.assertTrue(firstValuesArgument == listOfGitHubRepo)

            Assert.assertTrue(secondValue is LoadFavouriteInfoFromDbAction)
            val secondValuesArgument = (secondValue as LoadFavouriteInfoFromDbAction).userName
            Assert.assertTrue(secondValuesArgument == USER_NAME)
        }
    }

    @Test
    fun testRepoMiddlewareLoadReposAction_FailedLoading() {
        //GIVEN
        val mockEndpoint: GitHubEndpoint = mock()
        val mockRepository: Repository<GitHubRepoEntity> = mock()
        underTest = RepoMiddleware(mockEndpoint, mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val action = LoadReposAction(USER_NAME)
        val mockDispatchFunction: DispatchFunction = mock()
        Mockito.`when`(mockEndpoint.getRepos(USER_NAME)).thenReturn(Single.error(IOException()))
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockStore).dispatch(any<GitHubReposFailedAction>())
    }

    @Test
    fun testRepoMiddlewareLoadFavouriteInfoFromDbAction_SuccessLoading() {
        //GIVEN
        val mockEndpoint: GitHubEndpoint = mock()
        val mockRepository: Repository<GitHubRepoEntity> = mock()
        underTest = RepoMiddleware(mockEndpoint, mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val action = LoadFavouriteInfoFromDbAction(USER_NAME)
        val mockDispatchFunction: DispatchFunction = mock()
        val entity = GitHubRepoEntity(USER_NAME, REPO_NAME)
        val listOfEntity = listOf(entity)
        Mockito.`when`(mockRepository.query(any<GitRepoSpecification>())).thenReturn(Observable.just(listOfEntity))
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is FavouriteLoadedFromDbAction)
            val firstValuesArgument = (firstValue as FavouriteLoadedFromDbAction).resultMap
            val originalMap = listOfEntity.map { it.repoName to it }.toMap()
            Assert.assertTrue(firstValuesArgument == originalMap)
        }
    }

    @Test
    fun testRepoMiddlewareLoadFavouriteInfoFromDbAction_FailedLoading() {
        //GIVEN
        val mockEndpoint: GitHubEndpoint = mock()
        val mockRepository: Repository<GitHubRepoEntity> = mock()
        underTest = RepoMiddleware(mockEndpoint, mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val action = LoadFavouriteInfoFromDbAction(USER_NAME)
        val mockDispatchFunction: DispatchFunction = mock()
        Mockito.`when`(mockRepository.query(any<GitRepoSpecification>())).thenReturn(Observable.error(IOException()))
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockStore).dispatch(any<GitHubReposFailedAction>())
    }

    @Test
    fun testRepoMiddlewareSaveFavouriteAction() {
        //GIVEN
        val mockEndpoint: GitHubEndpoint = mock()
        val mockRepository: Repository<GitHubRepoEntity> = mock()
        underTest = RepoMiddleware(mockEndpoint, mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val action = SaveFavouriteAction(USER_NAME, REPO_NAME)
        val mockDispatchFunction: DispatchFunction = mock()
        Mockito.`when`(mockRepository.add(GitHubRepoEntity(USER_NAME, REPO_NAME))).thenReturn(Completable.complete())
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is SetFavouriteAction)
            val firstValuesArgument = (firstValue as SetFavouriteAction).repoName
            Assert.assertTrue(firstValuesArgument == REPO_NAME)
        }
    }

    @Test
    fun testRepoMiddlewareRemoveFavouriteAction() {
        //GIVEN
        val mockEndpoint: GitHubEndpoint = mock()
        val mockRepository: Repository<GitHubRepoEntity> = mock()
        underTest = RepoMiddleware(mockEndpoint, mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val action = RemoveFavouriteAction(USER_NAME, REPO_NAME)
        val mockDispatchFunction: DispatchFunction = mock()
        Mockito.`when`(mockRepository.remove(GitHubRepoEntity(USER_NAME, REPO_NAME))).thenReturn(Completable.complete())
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is ClearFavouriteAction)
            val firstValuesArgument = (firstValue as ClearFavouriteAction).repoName
            Assert.assertTrue(firstValuesArgument == REPO_NAME)
        }
    }
}