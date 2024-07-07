package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName

data class RewardWrapperResponse(
    @SerializedName("prizeList")
    val rewardList: List<RewardResponse>?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("eventEndDateTime")
    val eventEndDateTime: String?,
)
