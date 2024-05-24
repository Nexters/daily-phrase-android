package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ModalResponse
import retrofit2.http.GET

interface ModalApiService {
    @GET("/api/v1/modals")
    suspend fun getModals(): ApiResponse<BaseResponse<List<ModalResponse>>>
}
