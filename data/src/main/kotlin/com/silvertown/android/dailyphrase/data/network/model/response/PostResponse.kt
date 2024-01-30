package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.domain.model.Post

data class PostResponse(
    @SerializedName("content")
    val content: String,
    @SerializedName("imageRatio")
    val imageRatio: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("phraseId")
    val phraseId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("viewCount")
    val viewCount: Int,
    @SerializedName("isLike")
    val isLike: Boolean,
    @SerializedName("isFavorite")
    val isFavorite: Boolean,
)

fun PostResponse.toEntity(): PostEntity {
    return PostEntity(
        phraseId = phraseId,
        content = content,
        title = title,
        imageUrl = imageUrl,
        imageRatio = imageRatio,
        viewCount = viewCount,
        likeCount = likeCount,
        isLike = isLike,
        isFavorite = isFavorite
    )
}

fun PostResponse.toDomainModel(): Post {
    return Post(
        content = content,
        imageRatio = imageRatio,
        imageUrl = imageUrl,
        likeCount = likeCount,
        phraseId = phraseId,
        title = title,
        viewCount = viewCount,
        isLike = isLike,
        isFavorite = isFavorite
    )
}