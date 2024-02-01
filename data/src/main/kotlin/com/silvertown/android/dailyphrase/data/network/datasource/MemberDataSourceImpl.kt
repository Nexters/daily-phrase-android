package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.KakaoTokenRequest
import com.silvertown.android.dailyphrase.data.network.model.response.MemberResponse
import com.silvertown.android.dailyphrase.data.network.model.response.SignInTokenResponse
import com.silvertown.android.dailyphrase.data.network.service.MemberApiService
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApiService: MemberApiService,
) : MemberDataSource {

    override suspend fun getMemberData(memberId: Long): ApiResponse<MemberResponse> =
        memberApiService.getMemberData(memberId)

    override suspend fun deleteMember(memberId: Long): ApiResponse<MemberResponse> =
        memberApiService.deleteMember(memberId)

    override suspend fun getSignInToken(token: String): ApiResponse<SignInTokenResponse> {
        val body = KakaoTokenRequest(identityToken = token)
        return memberApiService.signInWithKaKaoTokenViaServer(body)
    }
}
