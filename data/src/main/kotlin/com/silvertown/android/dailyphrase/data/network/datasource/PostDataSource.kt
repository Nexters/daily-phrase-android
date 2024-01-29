package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse

interface PostDataSource {
    suspend fun getPosts(
        page: Int,
        size: Int,
    ): ApiResponse<BasePostResponse>
}
