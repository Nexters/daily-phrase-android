package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.ShareEventRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ShareEventResponse
import com.silvertown.android.dailyphrase.data.network.model.response.SharedCountResponse

interface ShareDataSource {
    suspend fun logShareEvent(
        data: ShareEventRequest,
    ): ApiResponse<BaseResponse<ShareEventResponse>>

    suspend fun getSharedCount(): ApiResponse<BaseResponse<SharedCountResponse>>
}
