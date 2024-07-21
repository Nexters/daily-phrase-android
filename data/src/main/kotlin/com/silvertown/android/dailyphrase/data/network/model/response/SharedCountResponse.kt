package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.SharedCountModel

data class SharedCountResponse(
    @SerializedName("shareCount")
    val shareCount: Int?,
    @SerializedName("date")
    val date: String?,
)

fun SharedCountResponse.toDomainModel(): SharedCountModel {
    return SharedCountModel(
        sharedCount = shareCount ?: 0
    )
}