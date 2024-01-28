package com.silvertown.android.dailyphrase.data.network.model.response

import com.silvertown.android.dailyphrase.data.database.model.PostEntity

data class PostResponse(
    val id: Long,
    val title: String,
    val previewText: String,
    val imageUrl: String?,
    val viewCount: Long,
    val likeCount: Long,
    val isBookmarked: Boolean,
)

fun PostResponse.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        title = title,
        previewText = previewText,
        imageUrl = imageUrl,
        viewCount = viewCount,
        likeCount = likeCount,
        isBookmarked = isBookmarked
    )
}
