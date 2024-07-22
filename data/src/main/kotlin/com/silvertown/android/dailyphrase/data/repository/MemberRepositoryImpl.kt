package com.silvertown.android.dailyphrase.data.repository

import com.silvertown.android.dailyphrase.data.datastore.datasource.MemberPreferencesDataSource
import com.silvertown.android.dailyphrase.data.datastore.datasource.TokenDataSource
import com.silvertown.android.dailyphrase.data.network.common.toResultModel
import com.silvertown.android.dailyphrase.data.network.datasource.MemberDataSource
import com.silvertown.android.dailyphrase.data.network.model.response.toDomainModel
import com.silvertown.android.dailyphrase.domain.model.LoginState
import com.silvertown.android.dailyphrase.domain.model.Member
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.model.SignInToken
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberPreferencesDataSource: MemberPreferencesDataSource,
    private val memberDataSource: MemberDataSource,
    private val tokenDataSource: TokenDataSource,
) : MemberRepository {

    override val memberData: Flow<Member> =
        memberPreferencesDataSource.memberData

    override suspend fun getMemberName(): String {
        return memberPreferencesDataSource.memberData.first().name
    }

    override suspend fun setMemberData(
        memberId: Long,
        name: String,
        imageUrl: String,
    ) {
        memberPreferencesDataSource.setMember(memberId, name, imageUrl)
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        tokenDataSource.saveAccessToken(accessToken)
        tokenDataSource.saveRefreshToken(refreshToken)
    }

    override suspend fun getMemberData(): Result<Member> =
        memberDataSource
            .getMemberData(memberPreferencesDataSource.getMemberId())
            .toResultModel { it.result?.toDomainModel() }

    override suspend fun deleteMember(): Result<Member> {
        return memberDataSource
            .deleteMember(memberPreferencesDataSource.getMemberId())
            .toResultModel { it.result?.toDomainModel() }
    }

    override suspend fun signInWithKaKaoTokenViaServer(token: String): Result<SignInToken> =
        memberDataSource
            .getSignInToken(token = token)
            .toResultModel { it.result?.toDomainModel() }

    override suspend fun getLoginState(): LoginState {
        val loginState = tokenDataSource.getAccessToken()
        return LoginState(
            isLoggedIn = !loginState.isNullOrEmpty(),
            accessToken = loginState.orEmpty()
        )
    }

    override fun getLoginStateFlow(): Flow<LoginState> {
        return tokenDataSource.getAccessTokenFlow().map { accessToken ->
            LoginState(
                !accessToken.isNullOrEmpty(),
                accessToken.orEmpty()
            )
        }
    }

    override suspend fun deleteAccessToken() =
        tokenDataSource.deleteAccessToken()

    override suspend fun updateSharedCount(count: Int) {
        memberPreferencesDataSource.updateSharedCount(count)
    }

    override fun getSharedCountFlow(): Flow<Int> {
        return memberPreferencesDataSource.memberData.map {
            it.sharedCount
        }
    }
}
