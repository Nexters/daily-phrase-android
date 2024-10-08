package com.silvertown.android.dailyphrase.domain.repository

import com.silvertown.android.dailyphrase.domain.model.LoginState
import com.silvertown.android.dailyphrase.domain.model.Member
import com.silvertown.android.dailyphrase.domain.model.SignInToken
import com.silvertown.android.dailyphrase.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    val memberData: Flow<Member>

    suspend fun getMemberName(): String

    suspend fun setMemberData(
        memberId: Long,
        name: String,
        imageUrl: String,
    )

    suspend fun saveTokens(accessToken: String, refreshToken: String)

    suspend fun getMemberData(): Result<Member>

    suspend fun deleteMember(): Result<Member>

    suspend fun signInWithKaKaoTokenViaServer(
        token: String,
    ): Result<SignInToken>

    suspend fun getLoginState(): LoginState

    fun getLoginStateFlow(): Flow<LoginState>

    suspend fun deleteAccessToken()

    suspend fun updateSharedCount(count: Int)

    fun getSharedCountFlow(): Flow<Int>
}
