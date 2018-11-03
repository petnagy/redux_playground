package com.playground.redux.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Database
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Update
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.data.UserSearch
import io.reactivex.Maybe


@Database(entities = [(GitHubRepoEntity::class), (UserSearch::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gitRepoDao(): GitRepoDao

    abstract fun userSearchDao(): UserSearchDao
}

@Dao
interface GitRepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: GitHubRepoEntity)

    @Update
    fun update(item: GitHubRepoEntity)

    @Delete
    fun delete(item: GitHubRepoEntity): Int

    @Query("SELECT * FROM favourite_git_repo WHERE userName = :userName")
    fun query(userName: String): Maybe<List<GitHubRepoEntity>>
}

@Dao
interface UserSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: UserSearch)

    @Update
    fun update(item: UserSearch)

    @Delete
    fun delete(item: UserSearch): Int

    @Query("SELECT * FROM user_search ORDER BY timeStamp DESC")
    fun queryAll(): Maybe<List<UserSearch>>
}