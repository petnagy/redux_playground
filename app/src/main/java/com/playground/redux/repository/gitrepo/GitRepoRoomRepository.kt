package com.playground.redux.repository.gitrepo

import android.arch.persistence.room.Entity
import com.playground.redux.repository.Repository
import com.playground.redux.repository.Specification
import com.playground.redux.room.GitRepoDao
import io.reactivex.Observable

@Entity(tableName = "git_repo", primaryKeys = ["userName", "repoName"])
data class GitRepoEntity(val userName: String, val repoName: String, val favourite: Boolean)

class GitRepoRoomRepository(private val dao: GitRepoDao): Repository<GitRepoEntity> {

    override fun add(item: GitRepoEntity) {
        dao.insert(item)
    }

    override fun add(items: Iterable<GitRepoEntity>) {
        items.forEach { item -> dao.insert(item) }
    }

    override fun update(item: GitRepoEntity) {
        dao.update(item)
    }

    override fun remove(item: GitRepoEntity) {
        dao.delete(item)
    }

    override fun query(specification: Specification): Observable<List<GitRepoEntity>> {
        if (specification !is GitRepoSpecification) {
            throw IllegalStateException("Wrong specification!!!!")
        }
        return dao.query(specification.userName).toObservable()
    }

}