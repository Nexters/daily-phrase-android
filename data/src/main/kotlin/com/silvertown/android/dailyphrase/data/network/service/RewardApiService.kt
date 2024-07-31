package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.EventEnterRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.EventEnterResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardInfoResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardWrapperResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RewardApiService {
    @GET("/api/v1/events/prizes")
    suspend fun getRewards(): ApiResponse<BaseResponse<RewardWrapperResponse>>

    @GET("/api/v1/events/info")
    suspend fun getRewardInfo(): ApiResponse<BaseResponse<RewardInfoResponse>>

    @POST("/api/v1/events/enter")
    suspend fun postEventEnter(
        @Body prizeId: EventEnterRequest,
    ): ApiResponse<BaseResponse<EventEnterResponse>>
}
