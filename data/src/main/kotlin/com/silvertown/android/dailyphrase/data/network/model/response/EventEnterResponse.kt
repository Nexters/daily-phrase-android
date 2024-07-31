package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

data class EventEnterResponse(
    @SerializedName("prizeId")
    val prizeId: Int,
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("status")
    val status: Int
)
