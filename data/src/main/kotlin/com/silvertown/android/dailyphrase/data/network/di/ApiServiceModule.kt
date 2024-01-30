package com.silvertown.android.dailyphrase.data.network.di

import com.silvertown.android.dailyphrase.data.network.service.PostApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Singleton
    @Provides
    fun providePostApiService(
        @DefaultOkHttpClient okHttpclient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): PostApiService =
        retrofit
            .client(okHttpclient)
            .build()
            .create(PostApiService::class.java)
}
