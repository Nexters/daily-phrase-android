package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardInfoResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardWrapperResponse
import com.silvertown.android.dailyphrase.data.network.service.RewardApiService
import javax.inject.Inject

class RewardDataSourceImpl @Inject constructor(
    private val rewardApiService: RewardApiService,
) : RewardDataSource {
    override suspend fun getHomeRewards(): ApiResponse<BaseResponse<RewardWrapperResponse>> =
        rewardApiService.getHomeRewards()

    override suspend fun getRewardInfo(): ApiResponse<BaseResponse<RewardInfoResponse>> =
        rewardApiService.getRewardInfo()

}