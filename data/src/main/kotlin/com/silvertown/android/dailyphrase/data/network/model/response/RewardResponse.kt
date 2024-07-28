package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.RewardBanner

data class RewardResponse(
    @SerializedName("eventId")
    val eventId: Int?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("manufacturer")
    val manufacturer: String?,
    @SerializedName("myEntryCount")
    val myEntryCount: Int?,
    @SerializedName("myTicketCount")
    val myTicketCount: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("prizeId")
    val prizeId: Int?,
    @SerializedName("requiredTicketCount")
    val requiredTicketCount: Int?,
    @SerializedName("totalParticipantCount")
    val totalParticipantCount: Int?,
    @SerializedName("shortName")
    val shortName: String?,
    @SerializedName("totalEntryCount")
    val totalEntryCount: Int?,
)

fun RewardResponse.toRewardBannerDomainModel(): RewardBanner {
    return RewardBanner(
        eventId = eventId ?: 0,
        imageUrl = imageUrl.orEmpty(),
        manufacturer = manufacturer.orEmpty(),
        myEntryCount = myEntryCount ?: 0,
        name = name.orEmpty(),
        prizeId = prizeId ?: 0,
        requiredTicketCount = requiredTicketCount ?: 0,
        shortName = shortName.orEmpty(),
        totalParticipantCount = totalParticipantCount ?: 0,
        totalEntryCount = totalEntryCount ?: 0,
        myTicketCount = myTicketCount ?: 0,
    )
}
