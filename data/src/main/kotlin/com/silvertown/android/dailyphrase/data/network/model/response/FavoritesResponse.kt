package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.Favorites

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

fun FavoritesResponse.toDomainModel(): Favorites =
    Favorites(
        isFavorite = isFavorite ?: false,
        favoredAt = favoredAt,
        canceledAt = canceledAt,
        memberId = memberId ?: 0,
        phraseId = phraseId ?: 0
    )
