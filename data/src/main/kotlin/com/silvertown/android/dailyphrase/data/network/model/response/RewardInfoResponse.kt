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
    @SerializedName("eventStartDateTime")
    val eventStartDateTime: String?,
    @SerializedName("eventEndDateTime")
    val eventEndDateTime: String?,
    @SerializedName("eventWinnerAnnouncementDateTime")
    val eventWinnerAnnouncementDateTime: String?,
)

fun RewardInfoResponse.toDomainModel(): RewardInfo {
    return RewardInfo(
        eventId = eventId,
        name = name.orEmpty(),
        eventStartDateTime = eventStartDateTime.toLocalDateTimeOrNull(),
        eventEndDateTime = eventEndDateTime.toLocalDateTimeOrNull(),
        eventWinnerAnnouncementDateTime = eventWinnerAnnouncementDateTime.toLocalDateTimeOrNull()
    )
}

fun String?.toLocalDateTimeOrNull(): LocalDateTime? {
    return this?.let {
        LocalDateTime.parse(it, DateTimeUtils.localDateTimeFormatter)
    }
}
