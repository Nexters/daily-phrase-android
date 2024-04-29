package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.ShareEvent

data class ShareEventResponse(
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("phraseId")
    val phraseId: Long,
    @SerializedName("sharedAt")
    val sharedAt: String,
)

fun ShareEventResponse.toDomainModel(): ShareEvent =
    ShareEvent(
        memberId = memberId,
        phraseId = phraseId,
        sharedAt = sharedAt,
    )
