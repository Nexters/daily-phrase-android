package com.silvertown.android.dailyphrase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity

@Database(
    entities = [
        PostEntity::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)

abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
