package com.playground.redux.repository.usersearch

import com.playground.redux.data.UserSearch
import com.playground.redux.repository.Repository
import com.playground.redux.repository.Specification
import com.playground.redux.room.UserSearchDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserSearchRoomRepository(private val dao: UserSearchDao): Repository<UserSearch> {

    override fun add(item: UserSearch) {
        Completable.fromAction { dao.insert(item) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun add(items: Iterable<UserSearch>) {
        Completable.fromAction { items.forEach { item -> dao.insert(item) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun update(item: UserSearch) {
        Completable.fromAction { dao.update(item) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun remove(item: UserSearch) {
        Completable.fromAction { dao.delete(item) }
                .subscribeOn(Schedulers.io())
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