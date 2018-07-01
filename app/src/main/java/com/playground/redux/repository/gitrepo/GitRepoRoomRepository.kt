package com.playground.redux.repository.gitrepo

import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.repository.Repository
import com.playground.redux.repository.Specification
import com.playground.redux.room.GitRepoDao
import io.reactivex.Completable
import io.reactivex.Observable


class GitRepoRoomRepository(private val dao: GitRepoDao) : Repository<GitHubRepoEntity> {

    override fun add(item: GitHubRepoEntity): Completable {
        return Completable.fromAction { dao.insert(item) }
    }

    override fun add(items: Iterable<GitHubRepoEntity>): Completable {
        return Completable.fromAction { items.forEach { item -> dao.insert(item) } }
    }

    override fun update(item: GitHubRepoEntity): Completable {
        return Completable.fromAction { dao.update(item) }
    }

    override fun remove(item: GitHubRepoEntity): Completable {
        return Completable.fromAction { dao.delete(item) }
    }

    override fun query(specification: Specification): Observable<List<GitHubRepoEntity>> {
        if (specification !is GitRepoSpecification) {
            throw IllegalStateException("Wrong specification!!!!")
        }
        return dao.query(specification.userName).toObservable()
    }

}