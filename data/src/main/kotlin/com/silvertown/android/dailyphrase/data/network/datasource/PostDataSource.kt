package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.PostsResponse

interface PostDataSource {
    suspend fun getPosts(
        page: Int,
        limit: Int,
    ): ApiResponse<PostsResponse>
}
