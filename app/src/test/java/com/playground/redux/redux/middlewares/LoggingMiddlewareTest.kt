package com.playground.redux.redux.middlewares

import com.nhaarman.mockitokotlin2.mock
import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.playground.redux.redux.appstate.AppState
import org.junit.Test
import org.mockito.Mockito

class LoggingMiddlewareTest {

    private lateinit var underTest: LoggingMiddleware

    @Test
    fun testLoggingMiddleware_CallNextDispatchFunction() {
        //GIVEN
        underTest = LoggingMiddleware()

        //WHEN
        val mockStore: Store<AppState> = mock()
        val mockDispatchFunction = Mockito.mock(DispatchFunction::class.java)
        val action: Action = mock()
        underTest.invoke(mockStore, action, mockDispatchFunction)

        //THEN
        Mockito.verify(mockDispatchFunction).dispatch(action)
    }

}