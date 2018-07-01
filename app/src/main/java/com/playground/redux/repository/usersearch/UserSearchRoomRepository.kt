package com.playground.redux.repository.usersearch

import com.playground.redux.data.UserSearch
import com.playground.redux.repository.Repository
import com.playground.redux.repository.Specification
import com.playground.redux.room.UserSearchDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserSearchRoomRepository(private val dao: UserSearchDao) : Repository<UserSearch> {

    override fun add(item: UserSearch): Completable {
        return Completable.fromAction { dao.insert(item) }
    }

    override fun add(items: Iterable<UserSearch>): Completable {
        return Completable.fromAction { items.forEach { item -> dao.insert(item) } }
    }

    override fun update(item: UserSearch): Completable {
        return Completable.fromAction { dao.update(item) }
    }

    override fun remove(item: UserSearch): Completable {
        return Completable.fromAction { dao.delete(item) }
    }

    override fun query(specification: Specification): Observable<List<UserSearch>> {
        if (specification !is GetAllRecordsSpecification) {
            throw IllegalStateException("Wrong specification!!!!")
        }
        return dao.queryAll().toObservable()
    }

}