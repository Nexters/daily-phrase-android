package com.silvertown.android.dailyphrase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.silvertown.android.dailyphrase.data.util.LocalDateTimeConverters
import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.model.PostEntity

@Database(
    entities = [
        PostEntity::class,
    ],
    version = 2,
    autoMigrations = [],
    exportSchema = true,
)

@TypeConverters(LocalDateTimeConverters::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
