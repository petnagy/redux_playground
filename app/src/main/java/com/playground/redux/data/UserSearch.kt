package com.playground.redux.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user_search")
data class UserSearch(@PrimaryKey val userName: String, val timeStamp: Long)