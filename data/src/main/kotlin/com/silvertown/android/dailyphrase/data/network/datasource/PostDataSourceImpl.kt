package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.service.PostApiService
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val postApiService: PostApiService,
) : PostDataSource {

    override suspend fun getPosts(
        page: Int,
        size: Int,
    ): ApiResponse<BasePostResponse> =
        postApiService.getPosts(page, size)

}
