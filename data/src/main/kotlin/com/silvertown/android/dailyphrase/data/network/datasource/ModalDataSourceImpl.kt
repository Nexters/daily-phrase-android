package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.ModalResponse
import com.silvertown.android.dailyphrase.data.network.service.ModalApiService
import javax.inject.Inject

class ModalDataSourceImpl @Inject constructor(
    private val modalApiService: ModalApiService,
) : ModalDataSource {
    override suspend fun getModals(): ApiResponse<BaseResponse<List<ModalResponse>>> {
        return modalApiService.getModals()
    }
}
