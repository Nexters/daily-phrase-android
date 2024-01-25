package com.silvertown.android.dailyphrase.domain.model

data class Post(
    val id: Long,
    val title: String,
    val previewText: String,
    val imageUrl: String?,
    val viewCount: Long,
    val likeCount: Long,
    val isBookmarked: Boolean,
)
