package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.KakaoTokenRequest
import com.silvertown.android.dailyphrase.data.network.model.response.MemberResponse
import com.silvertown.android.dailyphrase.data.network.model.response.SignInTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface MemberApiService {

    @POST("/api/v1/members/login/kakao")
    suspend fun signInWithKaKaoTokenViaServer(
        @Body body: KakaoTokenRequest,
    ): ApiResponse<SignInTokenResponse>

    @POST("/api/auth/access")
    suspend fun replaceToken(
        @Header("Refresh-Token") token: String,
    ): Response<SignInTokenResponse>

    @DELETE("/api/v1/members/{id}")
    suspend fun deleteMember(
        @Path("id") memberId: Long,
    ): ApiResponse<MemberResponse>

    @GET("api/v1/members/{id}")
    suspend fun getMemberData(
        @Path("id") memberId: Long,
    ): ApiResponse<MemberResponse>

}
