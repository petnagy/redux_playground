package com.playground.redux.repository

import io.reactivex.Completable
import io.reactivex.Observable

interface Repository<T> {

    fun add(item: T): Completable

    fun add(items: Iterable<T>): Completable

    fun update(item: T): Completable

    fun remove(item: T): Completable

    fun query(specification: Specification): Observable<List<T>>
}