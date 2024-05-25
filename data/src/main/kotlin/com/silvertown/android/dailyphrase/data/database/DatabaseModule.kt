package com.silvertown.android.dailyphrase.data.database

import android.content.Context
import androidx.room.Room
import com.silvertown.android.dailyphrase.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesPostDatabase(@ApplicationContext context: Context): PostDatabase =
        Room.databaseBuilder(
            context,
            PostDatabase::class.java,
            "post-database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Named("app_version")
    fun provideAppVersion(): String {
        return BuildConfig.APP_VERSION
    }
}
