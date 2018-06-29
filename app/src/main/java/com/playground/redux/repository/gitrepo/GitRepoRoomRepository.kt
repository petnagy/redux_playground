package com.playground.redux.repository.gitrepo

import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.repository.Repository
import com.playground.redux.repository.Specification
import com.playground.redux.room.GitRepoDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class GitRepoRoomRepository(private val dao: GitRepoDao) : Repository<GitHubRepoEntity> {

    override fun add(item: GitHubRepoEntity) {
        Completable.fromAction {
            dao.insert(item)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun add(items: Iterable<GitHubRepoEntity>) {
        Completable.fromAction {
            items.forEach { item -> dao.insert(item) }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun update(item: GitHubRepoEntity) {
        Completable.fromAction {
            dao.update(item)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    override fun remove(item: GitHubRepoEntity): Single<Int> {
        return Single.fromCallable { dao.delete(item) }
    }

    override fun query(specification: Specification): Observable<List<GitHubRepoEntity>> {
        if (specification !is GitRepoSpecification) {
            throw IllegalStateException("Wrong specification!!!!")
        }
        return dao.query(specification.userName).toObservable()
    }

}