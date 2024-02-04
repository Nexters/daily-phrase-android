package com.silvertown.android.dailyphrase.domain.model

data class Like(
    val isLike: Boolean,
    val likedAt: String?,
    val canceledAt: String?,
    val memberId: Long,
    val phraseId: Long,
    val likeCount: Int,
)
