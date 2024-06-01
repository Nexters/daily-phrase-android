package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.RewardWrapperResponse

interface RewardDataSource {
    suspend fun getHomeRewards(): ApiResponse<BaseResponse<RewardWrapperResponse>>
}
