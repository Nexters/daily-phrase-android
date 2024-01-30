package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

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
