package com.silvertown.android.dailyphrase.domain.model

data class Favorites(
    val isFavorite: Boolean,
    val favoredAt: String?,
    val canceledAt: String?,
    val memberId: Long,
    val phraseId: Long,
)
