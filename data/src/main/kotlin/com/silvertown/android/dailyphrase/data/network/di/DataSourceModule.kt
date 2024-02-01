package com.silvertown.android.dailyphrase.data.network.di

import com.silvertown.android.dailyphrase.data.network.datasource.MemberDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.MemberDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.service.MemberApiService
import com.silvertown.android.dailyphrase.data.network.service.PostApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun providePostDataSource(postApiService: PostApiService): PostDataSource =
        PostDataSourceImpl(postApiService)

    @Provides
    @Singleton
    fun provideMemberDataSource(memberApiService: MemberApiService): MemberDataSource =
        MemberDataSourceImpl(memberApiService)

}
