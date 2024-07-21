package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.ShareEventRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ShareEventResponse
import com.silvertown.android.dailyphrase.data.network.model.response.SharedCountResponse
import com.silvertown.android.dailyphrase.data.network.service.ShareApiService
import javax.inject.Inject

class ShareDataSourceImpl @Inject constructor(
    private val shareApiService: ShareApiService,
) : ShareDataSource {

    override suspend fun logShareEvent(data: ShareEventRequest): ApiResponse<BaseResponse<ShareEventResponse>> {
        return shareApiService.logShareEvent(data = data)
    }

    override suspend fun getSharedCount(): ApiResponse<BaseResponse<SharedCountResponse>> {
        return shareApiService.getSharedCount()
    }


}
