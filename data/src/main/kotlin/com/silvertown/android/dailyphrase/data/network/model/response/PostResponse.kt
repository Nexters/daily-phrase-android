package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.data.database.model.PostEntity

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
)

fun PostResponse.toEntity(): PostEntity {
    return PostEntity(
        phraseId = phraseId,
        content = content,
        title = title,
        imageUrl = imageUrl,
        imageRatio = imageRatio,
        viewCount = viewCount,
        likeCount = likeCount
    )
}
