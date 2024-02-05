package com.silvertown.android.dailyphrase.domain.repository

import androidx.paging.PagingData
import com.silvertown.android.dailyphrase.domain.model.Bookmark
import com.silvertown.android.dailyphrase.domain.model.Favorites
import com.silvertown.android.dailyphrase.domain.model.Like
import com.silvertown.android.dailyphrase.domain.model.Post
import kotlinx.coroutines.flow.Flow
import com.silvertown.android.dailyphrase.domain.model.Result

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>

    suspend fun getPost(
        phraseId: Long,
    ): Result<Post>

    suspend fun saveLike(
        phraseId: Long,
    ): Result<Like>

    suspend fun deleteLike(
        phraseId: Long,
    ): Result<Like>

    suspend fun getFavorites(): Flow<Result<Bookmark>>

    suspend fun saveFavorites(
        phraseId: Long,
    ): Result<Favorites>

    suspend fun deleteFavorites(
        phraseId: Long,
    ): Result<Favorites>

    suspend fun updateCounts(
        phraseId: Long,
        likeCount: Int,
        viewCount: Int,
    )

    suspend fun updateLikeState(
        phraseId: Long,
        isLike: Boolean,
        count: Int,
    )

    suspend fun updateFavoriteState(
        phraseId: Long,
        isFavorite: Boolean,
    )

}
