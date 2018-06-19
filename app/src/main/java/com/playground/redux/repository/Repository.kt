package com.playground.redux.repository

import io.reactivex.Observable

interface Repository<T> {

    fun add(item: T)

    fun add(items: Iterable<T>)

    fun update(item: T)

    fun remove(item: T)

    fun remove(specification: Specification)

    fun query(specification: Specification): Observable<List<T>>
}