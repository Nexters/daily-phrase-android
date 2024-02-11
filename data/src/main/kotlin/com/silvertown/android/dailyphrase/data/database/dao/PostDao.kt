package com.silvertown.android.dailyphrase.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.silvertown.android.dailyphrase.data.database.model.PostEntity

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("SELECT * FROM post_source ORDER BY createdAt DESC")
    fun pagingSource(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM post_source")
    suspend fun deletePosts()

    @Transaction
    suspend fun savePostsAndDeleteIfRequired(
        posts: List<PostEntity>,
        shouldDelete: Boolean,
    ) {
        if (shouldDelete) deletePosts()
        insertPosts(posts)
    }

    @Query("UPDATE post_source SET isLike = :isLike, likeCount = :count WHERE phraseId = :phraseId")
    suspend fun updateLikeState(phraseId: Long, isLike: Boolean, count: Int)

    @Query("UPDATE post_source SET isFavorite = :isFavorite WHERE phraseId = :phraseId")
    suspend fun updateFavoriteState(phraseId: Long, isFavorite: Boolean)

    @Query("UPDATE post_source SET likeCount = :likeCount, viewCount = :viewCount WHERE phraseId = :phraseId")
    suspend fun updateCounts(phraseId: Long, likeCount: Int, viewCount: Int)

}
