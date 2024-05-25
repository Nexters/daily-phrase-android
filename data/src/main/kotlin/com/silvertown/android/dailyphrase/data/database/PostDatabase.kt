package com.silvertown.android.dailyphrase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.silvertown.android.dailyphrase.data.util.LocalDateTimeConverters
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.dao.RemoteKeysDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.data.database.model.RemoteKeys

@Database(
    entities = [
        PostEntity::class,
        RemoteKeys::class
    ],
    version = 3,
    autoMigrations = [],
    exportSchema = true,
)

@TypeConverters(LocalDateTimeConverters::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
