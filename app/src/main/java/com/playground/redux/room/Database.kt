package com.playground.redux.room

import android.arch.persistence.room.*
import com.playground.redux.data.GitHubRepoEntity
import io.reactivex.Maybe


@Database(entities = [(GitHubRepoEntity::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gitRepoDao(): GitRepoDao

}

@Dao
interface GitRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: GitHubRepoEntity)

    @Update
    fun update(item: GitHubRepoEntity)

    @Delete
    fun delete(item: GitHubRepoEntity)

    @Query("SELECT * FROM favourite_git_repo WHERE userName = :userName")
    fun query(userName: String): Maybe<List<GitHubRepoEntity>>
}