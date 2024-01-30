package com.silvertown.android.dailyphrase.presentation.ui.detail

data class DetailUiState(
    val phraseId: Long = 0,
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val sharedCount: Int = 0,
    val viewCount: Int = 0,
    val isLike: Boolean = false,
    val isBookmark: Boolean = false,
)

