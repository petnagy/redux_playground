package com.playground.redux.repository

import io.reactivex.Observable
import io.reactivex.Single

interface Repository<T> {

    fun add(item: T)

    fun add(items: Iterable<T>)

    fun update(item: T)

    fun remove(item: T): Single<Int>

    fun query(specification: Specification): Observable<List<T>>
}