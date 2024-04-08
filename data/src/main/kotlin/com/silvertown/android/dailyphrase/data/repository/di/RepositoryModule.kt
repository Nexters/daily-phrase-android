package com.silvertown.android.dailyphrase.data.repository.di

import com.silvertown.android.dailyphrase.data.repository.MemberRepositoryImpl
import com.silvertown.android.dailyphrase.data.repository.PostRepositoryImpl
import com.silvertown.android.dailyphrase.data.repository.ShareRepositoryImpl
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Singleton
    @Binds
    fun bindPostRepository(
        postRepository: PostRepositoryImpl,
    ): PostRepository

    @Singleton
    @Binds
    fun bindMemberRepository(
        memberRepository: MemberRepositoryImpl,
    ): MemberRepository

    @Singleton
    @Binds
    fun bindShareRepository(
        shareRepository: ShareRepositoryImpl,
    ): ShareRepository
}
