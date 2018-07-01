package com.playground.redux.redux_impl

import java.util.concurrent.CopyOnWriteArrayList

interface Action

typealias Reducer<State> = (action: Action, state: State) -> State

typealias Middleware<State> = (store: Store<State>, action: Action, next: DispatchFunction) -> Unit

interface DispatchFunction {
    fun dispatch(action: Action)
}

interface StoreSubscriber<State> {
    fun newState(state: State)
}

class Store<State>(reducer: Reducer<State>, middlewareList: List<Middleware<State>>, initState: State) {

    private val dispatchers: MutableList<DispatchFunction> = arrayListOf()
    var state: State = initState
    private set(value) {
            field = value
            subscriptions.forEach { subscriber -> subscriber.newState(field) }
    }

    private var subscriptions: MutableList<StoreSubscriber<State>> = CopyOnWriteArrayList()

    init {
        dispatchers.add(object: DispatchFunction {
            @Synchronized
            override fun dispatch(action: Action) {
                val newState = reducer(action, state)
                if (state != newState) {
                    state = newState
                }
            }
        })
        middlewareList.reversed().map { middleware ->
            val next = dispatchers.first()
            dispatchers.add(0, object: DispatchFunction {
                override fun dispatch(action: Action) {
                    middleware(this@Store, action, next)
                }
            })
        }
    }

    fun dispatch(action: Action) {
        dispatchers.first().dispatch(action)
    }

    fun subscribe(subscriber: StoreSubscriber<State>) {
        subscriptions.add(subscriber)
        subscriber.newState(state)
    }

    fun unsubscribe(subscriber: StoreSubscriber<State>) {
        subscriptions.remove(subscriber)
    }
}