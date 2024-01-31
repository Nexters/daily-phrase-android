package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.data.database.model.PostEntity
import com.silvertown.android.dailyphrase.domain.model.Post

data class PostResponse(
    @SerializedName("content")
    val content: String?,
    @SerializedName("imageRatio")
    val imageRatio: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("likeCount")
    val likeCount: Int?,
    @SerializedName("phraseId")
    val phraseId: Long?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("viewCount")
    val viewCount: Int?,
    @SerializedName("isLike")
    val isLike: Boolean?,
    @SerializedName("isFavorite")
    val isFavorite: Boolean?,
)

fun PostResponse.toEntity(): PostEntity {
    return PostEntity(
        phraseId = phraseId ?: 0,
        content = content.orEmpty(),
        title = title.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        imageRatio = imageRatio.orEmpty(),
        viewCount = viewCount ?: 0,
        likeCount = likeCount ?: 0,
        isLike = isLike ?: false,
        isFavorite = isFavorite ?: false
    )
}

fun PostResponse.toDomainModel(): Post {
    return Post(
        content = content.orEmpty(),
        imageRatio = imageRatio.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        likeCount = likeCount ?: 0,
        phraseId = phraseId ?: 0,
        title = title.orEmpty(),
        viewCount = viewCount ?: 0,
        isLike = isLike ?: false,
        isFavorite = isFavorite ?: false
    )
}
