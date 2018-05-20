package com.playground.redux.redux_impl

import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread

interface Action

typealias Reducer<State> = (action: Action, state: State) -> State

interface NextDispatcher {
    fun dispatch(action: Action)
}

interface StoreSubscriber<State> {
    fun newState(state: State)
}

typealias Middleware<State> = (store: Store<State>, action: Action) -> Unit

class Store<State>(reducer: Reducer<State>, middlewareList: List<Middleware<State>>, initState: State) {

    private val dispatchers: MutableList<NextDispatcher> = arrayListOf()
    var state: State = initState
    private set(value) {
            field = value
            subscriptions.forEach { subscriber -> subscriber.newState(field) }
    }

    private var subscriptions: CopyOnWriteArrayList<StoreSubscriber<State>> = CopyOnWriteArrayList()

    init {
        dispatchers.add(object: NextDispatcher {
            override fun dispatch(action: Action) {
                state = reducer(action, state)
            }
        })
        middlewareList.map { middleware ->
            dispatchers.add(0, object: NextDispatcher {
                override fun dispatch(action: Action) {
                    middleware(this@Store, action)
                }
            })
        }
    }

    fun dispatch(action: Action) {
        thread {
            dispatchers.forEach { nextDispatch ->
                nextDispatch.dispatch(action)
            }
        }
    }

    fun subscribe(subscriber: StoreSubscriber<State>) {
        subscriptions.add(subscriber)
        subscriber.newState(state)
    }

    fun unsubscribe(subscriber: StoreSubscriber<State>) {
        subscriptions.remove(subscriber)
    }
}