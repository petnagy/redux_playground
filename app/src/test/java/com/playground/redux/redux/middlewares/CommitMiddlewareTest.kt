package com.playground.redux.redux.middlewares

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.playground.redux.data.Commit
import com.playground.redux.data.Committer
import com.playground.redux.data.GitCommit
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.redux.actions.CommitsLoadedFailedAction
import com.playground.redux.redux.actions.CommitsLoadedSuccessAction
import com.playground.redux.redux.actions.LoadCommitsAction
import com.playground.redux.redux.appstate.AppState
import io.reactivex.Single
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import java.io.IOException

class CommitMiddlewareTest {

    companion object {
        @ClassRule @JvmField
        val schedulers = RxImmediateSchedulerRule()

        const val USER_NAME = "userName"
        const val REPO_NAME = "repoName"
    }

    private lateinit var commitMiddleware: CommitMiddleware

    //https://imnotyourson.com/mockito-mocking-parameterized-class-in-kotlin/
    //inline fun <reified T: Any> mock() = Mockito.mock(T::class.java)

    @Test
    fun testCommitMiddlewareWithCommonAction_CallNextDispatchFunction() {
        //GIVEN
        var action = TestAction()
        val mockGitHubEndpoint: GitHubEndpoint = mock()
        commitMiddleware = CommitMiddleware(mockGitHubEndpoint)

        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        //WHEN
        commitMiddleware.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockDispatchFunction).dispatch(action)
    }

    @Test
    fun testCommitMiddlewareWithLoadCommitsAction_CallGetCommitsApiCall() {
        //GIVEN
        val action = LoadCommitsAction(USER_NAME, REPO_NAME)
        val mockGitHubEndpoint: GitHubEndpoint = mock()
        commitMiddleware = CommitMiddleware(mockGitHubEndpoint)

        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        Mockito.`when`(mockGitHubEndpoint.getCommits(USER_NAME, REPO_NAME)).thenReturn(Single.just(emptyList()))
        //WHEN
        commitMiddleware.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockGitHubEndpoint).getCommits(USER_NAME, REPO_NAME)
    }

    @Test
    fun testCommitMiddlewareWithLoadCommitsAction_ApiCallFailed() {
        //GIVEN
        val action = LoadCommitsAction(USER_NAME, REPO_NAME)
        val mockGitHubEndpoint: GitHubEndpoint = mock()
        commitMiddleware = CommitMiddleware(mockGitHubEndpoint)

        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        Mockito.`when`(mockGitHubEndpoint.getCommits(USER_NAME, REPO_NAME)).thenReturn(Single.error(IOException("Fake Exception")))
        //WHEN
        commitMiddleware.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockStore).dispatch(any<CommitsLoadedFailedAction>())
    }

    @Test
    fun testCommitMiddlewareWithLoadCommitsAction_ApiCallSuccess() {
        //GIVEN
        val action = LoadCommitsAction(USER_NAME, REPO_NAME)
        val mockGitHubEndpoint: GitHubEndpoint = mock()
        commitMiddleware = CommitMiddleware(mockGitHubEndpoint)

        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val gitCommit = GitCommit(Commit("message", Committer("name", "email", "date")))
        val listResult = listOf(gitCommit)
        Mockito.`when`(mockGitHubEndpoint.getCommits(USER_NAME, REPO_NAME)).thenReturn(Single.just(listResult))
        //WHEN
        commitMiddleware.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is CommitsLoadedSuccessAction)
            val firstValuesArgument = (firstValue as CommitsLoadedSuccessAction).commits
            Assert.assertTrue(firstValuesArgument == listResult)
        }
    }

    class TestAction: Action
}