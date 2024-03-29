package com.silvertown.android.dailyphrase.data.database

import com.silvertown.android.dailyphrase.data.database.dao.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesPostDao(
        database: PostDatabase,
    ): PostDao = database.postDao()
}
