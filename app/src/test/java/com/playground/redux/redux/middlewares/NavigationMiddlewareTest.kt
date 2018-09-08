package com.playground.redux.redux.middlewares

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.redux.actions.NextPageAction
import com.playground.redux.redux.actions.RepoSelectedAction
import com.playground.redux.redux.actions.UserSelectionAction
import com.playground.redux.redux.appstate.AppState
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class NavigationMiddlewareTest {

    private lateinit var navigationMiddleware: NavigationMiddleware

    @Test
    fun testUserSelectionAction_SendNextPageNAvigation() {
        //GIVEN
        val mockNavigator: Navigator = mock()
        navigationMiddleware = NavigationMiddleware(mockNavigator)
        val mockStore: Store<AppState> = mock()
        val action = UserSelectionAction("selectedUser")
        val mockNextDispatchFunction: DispatchFunction = mock()
        //WHEN
        Mockito.`when`(mockNavigator.goNextPage(Page.USER_SELECT_PAGE)).thenReturn(Page.REPO_SELECT_PAGE)
        navigationMiddleware.invoke(mockStore, action, mockNextDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is NextPageAction)
            val firstValuesArgument = (firstValue as NextPageAction).page
            Assert.assertTrue(firstValuesArgument == Page.REPO_SELECT_PAGE)
        }
    }

    @Test
    fun testRepoSelectedAction_SendNextPageNAvigation() {
        //GIVEN
        val mockNavigator: Navigator = mock()
        navigationMiddleware = NavigationMiddleware(mockNavigator)
        val mockStore: Store<AppState> = mock()
        val action = RepoSelectedAction("selectedRepo")
        val mockNextDispatchFunction: DispatchFunction = mock()
        //WHEN
        Mockito.`when`(mockNavigator.goNextPage(Page.REPO_SELECT_PAGE)).thenReturn(Page.COMMIT_LIST_PAGE)
        navigationMiddleware.invoke(mockStore, action, mockNextDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is NextPageAction)
            val firstValuesArgument = (firstValue as NextPageAction).page
            Assert.assertTrue(firstValuesArgument == Page.COMMIT_LIST_PAGE)
        }
    }
}