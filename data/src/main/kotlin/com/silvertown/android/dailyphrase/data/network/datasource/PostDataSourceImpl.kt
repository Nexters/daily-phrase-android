package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiFlow
import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.FavoritesRequest
import com.silvertown.android.dailyphrase.data.network.model.request.LikeRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BookmarkResponse
import com.silvertown.android.dailyphrase.data.network.model.response.FavoritesResponse
import com.silvertown.android.dailyphrase.data.network.model.response.LikeResponse
import com.silvertown.android.dailyphrase.data.network.model.response.PostResponse
import com.silvertown.android.dailyphrase.data.network.service.PostApiService
import kotlinx.coroutines.flow.Flow
import com.silvertown.android.dailyphrase.domain.model.Result
import javax.inject.Inject

class PostDataSourceImpl @Inject constructor(
    private val postApiService: PostApiService,
    private val refreshIntervalMs: Long = 5000,
) : PostDataSource {

    override suspend fun getPosts(
        page: Int,
        size: Int,
    ): BaseResponse<BasePostResponse> =
        postApiService.getPosts(page, size)

    override suspend fun getPost(
        phraseId: Long,
    ): ApiResponse<BaseResponse<PostResponse>> =
        postApiService.getPost(phraseId)

    override suspend fun saveLike(
        data: LikeRequest,
    ): ApiResponse<BaseResponse<LikeResponse>> =
        postApiService.saveLike(data)

    override suspend fun deleteLike(
        memberId: Long,
        phraseId: Long,
    ): ApiResponse<BaseResponse<LikeResponse>> =
        postApiService.deleteLike(memberId, phraseId)

    override fun getFavorites(
        memberId: Long,
    ): Flow<Result<BaseResponse<BookmarkResponse>>> = ApiFlow {
        postApiService.getFavorites(memberId)
    }

    override suspend fun saveFavorites(
        data: FavoritesRequest,
    ): ApiResponse<BaseResponse<FavoritesResponse>> =
        postApiService.saveFavorites(data)

    override suspend fun deleteFavorites(
        memberId: Long,
        phraseId: Long,
    ): ApiResponse<BaseResponse<FavoritesResponse>> =
        postApiService.deleteFavorites(memberId, phraseId)

}
