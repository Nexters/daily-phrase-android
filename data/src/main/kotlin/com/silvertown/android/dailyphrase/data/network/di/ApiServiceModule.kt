package com.silvertown.android.dailyphrase.data.network.di

import com.silvertown.android.dailyphrase.data.network.service.MemberApiService
import com.silvertown.android.dailyphrase.data.network.service.ModalApiService
import com.silvertown.android.dailyphrase.data.network.service.PostApiService
import com.silvertown.android.dailyphrase.data.network.service.ShareApiService
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
        @AuthOkHttpClient okHttpclient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): PostApiService =
        retrofit
            .client(okHttpclient)
            .build()
            .create(PostApiService::class.java)

    @Singleton
    @Provides
    fun provideMemberApiService(
        @AuthOkHttpClient okHttpclient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): MemberApiService =
        retrofit
            .client(okHttpclient)
            .build()
            .create(MemberApiService::class.java)

    @Singleton
    @Provides
    fun provideShareApiService(
        @AuthOkHttpClient okHttpclient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): ShareApiService =
        retrofit
            .client(okHttpclient)
            .build()
            .create(ShareApiService::class.java)

    @Singleton
    @Provides
    fun provideModalApiService(
        @AuthOkHttpClient okHttpclient: OkHttpClient,
        retrofit: Retrofit.Builder,
    ): ModalApiService =
        retrofit
            .client(okHttpclient)
            .build()
            .create(ModalApiService::class.java)
}
