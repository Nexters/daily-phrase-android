package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

data class FavoritesResponse(
    @SerializedName("isFavorite")
    val isFavorite: Boolean?,
    @SerializedName("favoredAt")
    val favoredAt: String?,
    @SerializedName("canceledAt")
    val canceledAt: String?,
    @SerializedName("memberId")
    val memberId: Long?,
    @SerializedName("phraseId")
    val phraseId: Long?,
)
