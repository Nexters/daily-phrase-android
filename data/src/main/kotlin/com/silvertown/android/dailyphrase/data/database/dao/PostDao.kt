package com.silvertown.android.dailyphrase.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import java.util.concurrent.CountDownLatch

@Dao
interface PostDao {
    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM post_source")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM post_source")
    suspend fun deletePosts()

    @Transaction
    suspend fun savePostsAndDeleteIfRequired(
        posts: List<PostEntity>,
        shouldDelete: Boolean,
    ) {
        if (shouldDelete) deletePosts()
        upsertPosts(posts)
    }

    @Query("UPDATE post_source SET isLike = :isLike, likeCount = :count WHERE phraseId = :phraseId")
    suspend fun updateLikeState(phraseId: Long, isLike: Boolean, count: Int)

    @Query("UPDATE post_source SET isFavorite = :isFavorite WHERE phraseId = :phraseId")
    suspend fun updateFavoriteState(phraseId: Long, isFavorite: Boolean)

    @Query("UPDATE post_source SET likeCount = :likeCount, viewCount = :viewCount WHERE phraseId = :phraseId")
    suspend fun updateCounts(phraseId: Long, likeCount: Int, viewCount: Int)

}
