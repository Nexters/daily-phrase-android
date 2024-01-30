package com.silvertown.android.dailyphrase.domain.model

data class Post(
    val phraseId: Long,
    val title: String,
    val content: String,
    val imageUrl: String,
    val imageRatio: String,
    val viewCount: Int,
    val likeCount: Int,
    val isLike: Boolean,
    val isFavorite: Boolean,
)
