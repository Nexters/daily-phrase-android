package com.silvertown.android.dailyphrase.data.database

import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import com.silvertown.android.dailyphrase.data.database.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun providesPostDao(
        database: PostDatabase,
    ): PostDao = database.postDao()

    @Provides
    fun providesRemoteKeysDao(
        database: PostDatabase,
    ): RemoteKeysDao = database.remoteKeysDao()
}
