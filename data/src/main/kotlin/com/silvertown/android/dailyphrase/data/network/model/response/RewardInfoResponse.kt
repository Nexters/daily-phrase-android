package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.data.util.DateTimeUtils
import com.silvertown.android.dailyphrase.domain.model.RewardInfo
import java.time.LocalDateTime

data class RewardInfoResponse(
    @SerializedName("eventId")
    val eventId: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("eventEndDateTime")
    val eventEndDateTime: String?,
)

fun RewardInfoResponse.toDomainModel(): RewardInfo {
    val parsedEndDate = eventEndDateTime?.let {
        LocalDateTime.parse(it, DateTimeUtils.localDateTimeFormatter)
    }

    return RewardInfo(
        eventId = eventId,
        name = name.orEmpty(),
        eventEndDateTime = parsedEndDate
    )
}
