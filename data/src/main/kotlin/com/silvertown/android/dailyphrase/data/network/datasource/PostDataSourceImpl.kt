package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.FavoritesRequest
import com.silvertown.android.dailyphrase.data.network.model.request.LikeRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.FavoritesResponse
import com.silvertown.android.dailyphrase.data.network.model.response.LikeResponse
import com.silvertown.android.dailyphrase.data.network.model.response.PostResponse
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

    override suspend fun getPost(phraseId: Long): ApiResponse<PostResponse> =
        postApiService.getPost(phraseId)

    override suspend fun saveLike(data: LikeRequest): ApiResponse<LikeResponse> =
        postApiService.saveLike(data)

    override suspend fun deleteLike(
        memberId: Long,
        phraseId: Long,
    ): ApiResponse<LikeResponse> =
        postApiService.deleteLike(memberId, phraseId)

    override suspend fun getFavorites(memberId: Long): ApiResponse<PostResponse> =
        postApiService.getFavorites(memberId)

    override suspend fun saveFavorites(data: FavoritesRequest): ApiResponse<FavoritesResponse> =
        postApiService.saveFavorites(data)

    override suspend fun deleteFavorites(
        memberId: Long,
        phraseId: Long,
    ): ApiResponse<FavoritesResponse> =
        postApiService.deleteFavorites(memberId, phraseId)

}
