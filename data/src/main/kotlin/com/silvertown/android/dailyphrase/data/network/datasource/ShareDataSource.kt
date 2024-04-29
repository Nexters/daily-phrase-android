package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.ShareEventRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ShareEventResponse

interface ShareDataSource {
    suspend fun logShareEvent(
        data: ShareEventRequest,
    ): ApiResponse<BaseResponse<ShareEventResponse>>
}
