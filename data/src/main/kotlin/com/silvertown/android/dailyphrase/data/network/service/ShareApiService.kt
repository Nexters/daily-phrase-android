package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.ShareEventRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ShareEventResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ShareApiService {
    @POST("/api/v1/shares")
    suspend fun logShareEvent(
        @Body data: ShareEventRequest,
    ): ApiResponse<BaseResponse<ShareEventResponse>>
}