package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApiService {

    @GET("/api/v1/phrases")
    fun getPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResponse<BasePostResponse>

}
