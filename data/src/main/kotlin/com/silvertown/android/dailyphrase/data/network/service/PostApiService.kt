package com.silvertown.android.dailyphrase.data.network.service

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import com.silvertown.android.dailyphrase.data.network.model.request.FavoritesRequest
import com.silvertown.android.dailyphrase.data.network.model.request.LikeRequest
import com.silvertown.android.dailyphrase.data.network.model.response.BasePostResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BaseResponse
import com.silvertown.android.dailyphrase.data.network.model.response.BookmarkResponse
import com.silvertown.android.dailyphrase.data.network.model.response.FavoritesResponse
import com.silvertown.android.dailyphrase.data.network.model.response.LikeResponse
import com.silvertown.android.dailyphrase.data.network.model.response.PostResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

    @GET("/api/v1/phrases")
    suspend fun getPosts(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): BaseResponse<BasePostResponse>

    @GET("/api/v1/phrases/{id}")
    suspend fun getPost(
        @Path("id") phraseId: Long,
    ): ApiResponse<BaseResponse<PostResponse>>

    @POST("/api/v1/likes")
    suspend fun saveLike(
        @Body data: LikeRequest,
    ): ApiResponse<BaseResponse<LikeResponse>>

    @DELETE("/api/v1/likes/members/{memberId}/phrases/{phraseId}")
    suspend fun deleteLike(
        @Path("memberId") memberId: Long,
        @Path("phraseId") phraseId: Long,
    ): ApiResponse<BaseResponse<LikeResponse>>

    @GET("/api/v1/favorites/members/{id}")
    suspend fun getFavorites(
        @Path("id") memberId: Long,
    ): ApiResponse<BaseResponse<BookmarkResponse>>

    @POST("/api/v1/favorites")
    suspend fun saveFavorites(
        @Body data: FavoritesRequest,
    ): ApiResponse<BaseResponse<FavoritesResponse>>

    @DELETE("/api/v1/favorites/members/{memberId}/phrases/{phraseId}")
    suspend fun deleteFavorites(
        @Path("memberId") memberId: Long,
        @Path("phraseId") phraseId: Long,
    ): ApiResponse<BaseResponse<FavoritesResponse>>

}
