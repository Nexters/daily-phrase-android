package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.Like

data class LikeResponse(
    @SerializedName("isLike")
    val isLike: Boolean?,
    @SerializedName("likedAt")
    val likedAt: String?,
    @SerializedName("canceledAt")
    val canceledAt: String?,
    @SerializedName("memberId")
    val memberId: Long?,
    @SerializedName("phraseId")
    val phraseId: Long?,
)

fun LikeResponse.toDomainModel(): Like =
    Like(
        isLike = isLike ?: false,
        likedAt = likedAt.orEmpty(),
        canceledAt = canceledAt.orEmpty(),
        memberId = memberId ?: 0,
        phraseId = phraseId ?: 0
    )
