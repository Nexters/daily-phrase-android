package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.CheckEntryResultResponse
import com.silvertown.android.dailyphrase.data.network.model.response.EventEnterResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardInfoResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardWrapperResponse
import com.silvertown.android.dailyphrase.data.network.model.response.WinnerPhoneNumberResponse

interface RewardDataSource {
    suspend fun getRewards(): ApiResponse<BaseResponse<RewardWrapperResponse>>
    suspend fun getRewardInfo(): ApiResponse<BaseResponse<RewardInfoResponse>>
    suspend fun postEventEnter(prizeId: Int): ApiResponse<BaseResponse<EventEnterResponse>>
    suspend fun postCheckEntryResult(prizeId: Int): ApiResponse<BaseResponse<CheckEntryResultResponse>>
    suspend fun postWinnerPhoneNumber(prizeId: Int, phoneNumber: String): ApiResponse<BaseResponse<WinnerPhoneNumberResponse>>
}
