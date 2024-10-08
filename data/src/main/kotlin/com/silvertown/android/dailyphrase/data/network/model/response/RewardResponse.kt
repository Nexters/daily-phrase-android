package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
import com.silvertown.android.dailyphrase.domain.model.RewardBanner

data class RewardResponse(
    @SerializedName("eventId")
    val eventId: Int?,
    @SerializedName("welcomeImageUrl")
    val welcomeImageUrl: String?,
    @SerializedName("bannerImageUrl")
    val bannerImageUrl: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("manufacturer")
    val manufacturer: String?,
    @SerializedName("myEntryCount")
    val myEntryCount: Int?,
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
    @SerializedName("prizeEntryResult")
    val prizeEntryResult: PrizeEntryResult,
) {
    data class PrizeEntryResult(
        @SerializedName("status")
        val status: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String?,
        @SerializedName("isChecked")
        val isChecked: Boolean,
    )
}

fun RewardResponse.toRewardBannerDomainModel(
    myTicketCount: Int?,
): RewardBanner {
    return RewardBanner(
        eventId = eventId ?: 0,
        welcomeImageUrl = welcomeImageUrl.orEmpty(),
        bannerImageUrl = bannerImageUrl.orEmpty(),
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

fun RewardResponse.toPrizeDomainModel(): PrizeInfo.Item {
    return PrizeInfo.Item(
        eventId = eventId ?: 0,
        welcomeImageUrl = bannerImageUrl.orEmpty(),
        bannerImageUrl = bannerImageUrl.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        manufacturer = manufacturer.orEmpty(),
        myEntryCount = myEntryCount ?: 0,
        name = name.orEmpty(),
        prizeId = prizeId ?: 0,
        requiredTicketCount = requiredTicketCount ?: 0,
        shortName = shortName.orEmpty(),
        totalParticipantCount = totalParticipantCount ?: 0,
        totalEntryCount = totalEntryCount ?: 0,
        entryResult = prizeEntryResult.toEntryResultDomainModel(),
    )
}

fun RewardResponse.PrizeEntryResult.toEntryResultDomainModel(): PrizeInfo.Item.EntryResult {
    return PrizeInfo.Item.EntryResult(
        status = PrizeInfo.Item.EntryResult.Status.ofValue(status),
        phoneNumber = phoneNumber,
        isChecked = isChecked,
    )
}
