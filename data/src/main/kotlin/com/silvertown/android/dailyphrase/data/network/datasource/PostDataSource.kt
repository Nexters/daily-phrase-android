package com.silvertown.android.dailyphrase.data.network.datasource

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.FavoritesRequest
import com.silvertown.android.dailyphrase.data.network.model.request.LikeRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BookmarkResponse
import com.silvertown.android.dailyphrase.data.network.model.response.FavoritesResponse
import com.silvertown.android.dailyphrase.data.network.model.response.LikeResponse
import com.silvertown.android.dailyphrase.data.network.model.response.PostResponse
import kotlinx.coroutines.flow.Flow
import com.silvertown.android.dailyphrase.domain.model.Result

interface PostDataSource {
    suspend fun getPosts(
        page: Int,
        size: Int,
    ): BaseResponse<BasePostResponse>

    suspend fun getPost(
        phraseId: Long,
    ): ApiResponse<BaseResponse<PostResponse>>

    suspend fun saveLike(
        data: LikeRequest,
    ): ApiResponse<BaseResponse<LikeResponse>>

    suspend fun deleteLike(
        memberId: Long,
        phraseId: Long,
    ): ApiResponse<BaseResponse<LikeResponse>>

    fun getFavorites(
        memberId: Long,
    ): Flow<Result<BaseResponse<BookmarkResponse>>>

    suspend fun saveFavorites(
        data: FavoritesRequest,
    ): ApiResponse<BaseResponse<FavoritesResponse>>

    suspend fun deleteFavorites(
        memberId: Long,
        phraseId: Long,
    ): ApiResponse<BaseResponse<FavoritesResponse>>
}
