package com.silvertown.android.dailyphrase.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.silvertown.android.dailyphrase.data.database.model.PostEntity

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
}
