package com.silvertown.android.dailyphrase.di

import android.content.Context
import com.silvertown.android.dailyphrase.data.network.TokenExpirationNotifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TokenExpirationModule {

    @Provides
    fun provideRefreshTokenExpirationNotifier(
        @ApplicationContext context: Context,
    ): TokenExpirationNotifier =
        RefreshTokenExpirationNotifier(context)
}
