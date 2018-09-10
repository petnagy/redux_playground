package com.playground.redux.redux.middlewares

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.playground.redux.data.UserSearch
import com.playground.redux.redux.actions.*
import com.playground.redux.redux.appstate.AppState
import com.playground.redux.repository.Repository
import com.playground.redux.repository.usersearch.GetAllRecordsSpecification
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mockito
import java.io.IOException

class UserMiddlewareTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()

        private const val USER_NAME = "userName"
    }

    private lateinit var underTest: UserMiddleware

    @Test
    fun testUserMiddlewareWithLoadPreviousSearchAction_SuccessCall() {
        //GIVEN
        val mockRepository: Repository<UserSearch> = mock()
        underTest = UserMiddleware(mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val action = LoadPreviousSearchAction()

        val userSearch = UserSearch(USER_NAME, 123456)
        val listOfUserSearch = listOf(userSearch)
        Mockito.`when`(mockRepository.query(any<GetAllRecordsSpecification>())).thenReturn(Observable.just(listOfUserSearch))

        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is PreviousSearchListAction)
            val firstValuesArgument = (firstValue as PreviousSearchListAction).prevUserSearches
            Assert.assertTrue(firstValuesArgument == listOfUserSearch)
        }
    }

    @Test
    fun testUserMiddlewareWithLoadPreviousSearchAction_FailedCall() {
        //GIVEN
        val mockRepository: Repository<UserSearch> = mock()
        underTest = UserMiddleware(mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val action = LoadPreviousSearchAction()
        Mockito.`when`(mockRepository.query(any<GetAllRecordsSpecification>())).thenReturn(Observable.error(IOException()))

        underTest.invoke(mockStore, action, mockDispatchFunction)
        //THEN

        //it is not implemented...
    }

    @Test
    fun testUserMiddlewareWithUserSelectionAction() {
        //GIVEN
        val mockRepository: Repository<UserSearch> = mock()
        underTest = UserMiddleware(mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val action = UserSelectionAction(USER_NAME)

        Mockito.`when`(mockRepository.add(any<UserSearch>())).thenReturn(Completable.complete())

        underTest.invoke(mockStore, action, mockDispatchFunction)
        //THEN

        argumentCaptor<Action>().apply {
            verify(mockStore, Mockito.times(1)).dispatch(capture())
            Assert.assertTrue(firstValue is AddHistoryAction)
            val firstValuesArgument = (firstValue as AddHistoryAction).selectedUser
            Assert.assertTrue(firstValuesArgument == USER_NAME)
        }
    }

    @Test
    fun testUserMiddlewareWithPreviousSearchDeleteAction() {
        //GIVEN
        val mockRepository: Repository<UserSearch> = mock()
        underTest = UserMiddleware(mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val userSearch = UserSearch(USER_NAME, 123456789)
        val action = PreviousSearchDeleteAction(userSearch)

        Mockito.`when`(mockRepository.remove(userSearch)).thenReturn(Completable.complete())

        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockStore).dispatch(any<LoadPreviousSearchAction>())
    }

    @Test
    fun testUserMiddlewareWithUndoUserSearchDeleteAction() {
        //GIVEN
        val mockRepository: Repository<UserSearch> = mock()
        underTest = UserMiddleware(mockRepository)

        //WHEN
        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val userSearch = UserSearch(USER_NAME, 123456789)
        val action = UndoUserSearchDeleteAction(userSearch)

        Mockito.`when`(mockRepository.add(userSearch)).thenReturn(Completable.complete())

        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockStore).dispatch(any<LoadPreviousSearchAction>())
    }
}