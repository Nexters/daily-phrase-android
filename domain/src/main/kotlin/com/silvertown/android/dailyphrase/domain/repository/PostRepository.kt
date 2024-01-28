package com.silvertown.android.dailyphrase.domain.repository

import androidx.paging.PagingData
import com.silvertown.android.dailyphrase.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>
}
