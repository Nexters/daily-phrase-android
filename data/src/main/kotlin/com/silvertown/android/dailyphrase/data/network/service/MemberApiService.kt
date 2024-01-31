package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.KakaoTokenRequest
import com.silvertown.android.dailyphrase.data.network.model.response.SignInTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MemberApiService {

    @POST("/api/auth")
    suspend fun signInWithKaKaoTokenViaServer(
        @Body body: KakaoTokenRequest,
    ): ApiResponse<SignInTokenResponse>

    @POST("/api/auth/access")
    suspend fun replaceToken(
        @Header("Refresh-Token") token: String,
    ): Response<SignInTokenResponse>


}

