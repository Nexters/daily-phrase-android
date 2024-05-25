package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.Modal

data class ModalResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("leftButtonMessage") val leftButtonMessage: String?,
    @SerializedName("rightButtonMessage") val rightButtonMessage: String?,
) {
    fun toDomainModel(): Modal {
        return Modal(
            id = id,
            type = type,
            imageUrl = imageUrl,
            leftButtonMessage = leftButtonMessage,
            rightButtonMessage = rightButtonMessage,
        )
    }
}
