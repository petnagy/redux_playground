package com.playground.redux.room

import android.arch.persistence.room.*
import com.playground.redux.repository.gitrepo.GitRepoEntity
import android.arch.persistence.room.OnConflictStrategy
import io.reactivex.Flowable


@Database(entities = [(GitRepoEntity::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gitRepoDao(): GitRepoDao

}

@Dao
interface GitRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: GitRepoEntity)

    @Update
    fun update(item: GitRepoEntity)

    @Delete
    fun delete(item: GitRepoEntity)

    @Query("SELECT * FROM git_repo WHERE userName = :userName")
    fun query(userName: String): Flowable<List<GitRepoEntity>>
}