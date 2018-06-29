package com.playground.redux.repository

import io.reactivex.Completable
import io.reactivex.Observable

interface Repository<T> {

    fun add(item: T): Completable

    fun add(items: Iterable<T>)

    fun update(item: T)

    fun remove(item: T): Completable

    fun query(specification: Specification): Observable<List<T>>
}