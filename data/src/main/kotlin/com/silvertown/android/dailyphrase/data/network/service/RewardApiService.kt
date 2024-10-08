package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.CheckEntryResultRequest
import com.silvertown.android.dailyphrase.data.network.model.request.EventEnterRequest
import com.silvertown.android.dailyphrase.data.network.model.request.WinnerPhoneNumberRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.CheckEntryResultResponse
import com.silvertown.android.dailyphrase.data.network.model.response.EventEnterResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardInfoResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardWrapperResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ShouldShowGetTicketPopup
import com.silvertown.android.dailyphrase.data.network.model.response.WinnerPhoneNumberResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RewardApiService {
    @GET("/api/v1/events/prizes")
    suspend fun getRewards(): ApiResponse<BaseResponse<RewardWrapperResponse>>

    @GET("/api/v1/events/info")
    suspend fun getRewardInfo(): ApiResponse<BaseResponse<RewardInfoResponse>>

    @POST("/api/v1/events/enter")
    suspend fun postEventEnter(
        @Body prizeId: EventEnterRequest,
    ): ApiResponse<BaseResponse<EventEnterResponse>>

    @POST("/api/v1/events/prizes/entry-result/check")
    suspend fun postCheckEntryResult(
        @Body prizeId: CheckEntryResultRequest,
    ): ApiResponse<BaseResponse<CheckEntryResultResponse>>

    @POST("/api/v1/events/prizes/{prizeId}/phone-number")
    suspend fun postWinnerPhoneNumber(
        @Body winnerPhoneNumberRequest: WinnerPhoneNumberRequest,
        @Path("prizeId") prizeId: Int,
    ): ApiResponse<BaseResponse<WinnerPhoneNumberResponse>>

    @GET("/api/v1/events/tickets/me")
    suspend fun getShouldShowTicketPopup(): ApiResponse<BaseResponse<ShouldShowGetTicketPopup>>
}
