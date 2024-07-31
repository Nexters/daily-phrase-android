package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.EventEnterRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.EventEnterResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardInfoResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardWrapperResponse
import com.silvertown.android.dailyphrase.data.network.service.RewardApiService
import javax.inject.Inject

class RewardDataSourceImpl @Inject constructor(
    private val rewardApiService: RewardApiService,
) : RewardDataSource {
    override suspend fun getRewards(): ApiResponse<BaseResponse<RewardWrapperResponse>> =
        rewardApiService.getRewards()

    override suspend fun getRewardInfo(): ApiResponse<BaseResponse<RewardInfoResponse>> =
        rewardApiService.getRewardInfo()

    override suspend fun postEventEnter(prizeId: Int): ApiResponse<BaseResponse<EventEnterResponse>> =
        rewardApiService.postEventEnter(EventEnterRequest(prizeId))
}