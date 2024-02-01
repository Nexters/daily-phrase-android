package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.MemberResponse
import com.silvertown.android.dailyphrase.data.network.model.response.SignInTokenResponse

interface MemberDataSource {
    suspend fun getMemberData(
        memberId: Long,
    ): ApiResponse<BaseResponse<MemberResponse>>

    suspend fun deleteMember(
        memberId: Long,
    ): ApiResponse<BaseResponse<MemberResponse>>

    suspend fun getSignInToken(
        token: String,
    ): ApiResponse<BaseResponse<SignInTokenResponse>>

}
