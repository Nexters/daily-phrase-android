package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.ShareEventRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ShareEventResponse
import com.silvertown.android.dailyphrase.data.network.model.response.SharedCountResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShareApiService {
    @POST("/api/v1/shares")
    suspend fun logShareEvent(
        @Body data: ShareEventRequest,
    ): ApiResponse<BaseResponse<ShareEventResponse>>

    @GET("/api/v1/shares/me")
    suspend fun getSharedCount(
        @Query("date") date: String? = null
    ): ApiResponse<BaseResponse<SharedCountResponse>>
}