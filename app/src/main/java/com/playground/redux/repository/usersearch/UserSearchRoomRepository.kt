package com.playground.redux.repository.usersearch

import com.playground.redux.data.UserSearch
import com.playground.redux.repository.Repository
import com.playground.redux.repository.Specification
import com.playground.redux.room.UserSearchDao
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserSearchRoomRepository(private val dao: UserSearchDao): Repository<UserSearch> {

    override fun add(item: UserSearch) {
        Single.fromCallable {
            dao.insert(item)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun add(items: Iterable<UserSearch>) {
        Single.fromCallable {
            items.forEach { item -> dao.insert(item) }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun update(item: UserSearch) {
        Single.fromCallable {
            dao.update(item)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun remove(item: UserSearch) {
        //Do nothing
    }

    override fun remove(specification: Specification) {
        if (specification !is HistoryItemDeleteSpecification) {
            throw IllegalStateException("Wrong specification!!!!")
        }
        Single.fromCallable {
            dao.delete(specification.userName)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun query(specification: Specification): Observable<List<UserSearch>> {
        if (specification !is GetAllRecordsSpecification) {
            throw IllegalStateException("Wrong specification!!!!")
        }
        return dao.queryAll().toObservable()
    }

}