package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.PrizeInfo

data class RewardWrapperResponse(
    @SerializedName("prizeList")
    val rewardList: List<RewardResponse>?,
    @SerializedName("myTicketCount")
    val myTicketCount: Int?,
)

fun RewardWrapperResponse.toDomainModel(): PrizeInfo {
    return PrizeInfo(
        myTicketCount = myTicketCount ?: 0,
        items = rewardList?.map { it.toPrizeDomainModel() } ?: emptyList(),
    )
}
