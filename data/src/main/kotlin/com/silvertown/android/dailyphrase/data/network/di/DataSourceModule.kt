package com.silvertown.android.dailyphrase.data.network.di

import com.silvertown.android.dailyphrase.data.network.datasource.MemberDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.MemberDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.datasource.ModalDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.ModalDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.PostDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.datasource.RewardDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.RewardDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.datasource.ShareDataSource
import com.silvertown.android.dailyphrase.data.network.datasource.ShareDataSourceImpl
import com.silvertown.android.dailyphrase.data.network.service.MemberApiService
import com.silvertown.android.dailyphrase.data.network.service.ModalApiService
import com.silvertown.android.dailyphrase.data.network.service.PostApiService
import com.silvertown.android.dailyphrase.data.network.service.RewardApiService
import com.silvertown.android.dailyphrase.data.network.service.ShareApiService
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

    @Provides
    @Singleton
    fun provideShareDataSource(shareApiService: ShareApiService): ShareDataSource =
        ShareDataSourceImpl(shareApiService)

    @Provides
    @Singleton
    fun provideModalDataSource(modalApiService: ModalApiService): ModalDataSource =
        ModalDataSourceImpl(modalApiService)

    @Provides
    @Singleton
    fun provideRewardDataSource(rewardApiService: RewardApiService): RewardDataSource =
        RewardDataSourceImpl(rewardApiService)
}
