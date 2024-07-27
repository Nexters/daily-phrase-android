package com.silvertown.android.dailyphrase.domain.model

import java.time.LocalDateTime

data class RewardInfo(
    val eventId: Int?,
    val name: String,
    val eventStartDateTime: LocalDateTime?,
    val eventEndDateTime: LocalDateTime?,
    val eventWinnerAnnouncementDateTime: LocalDateTime?,
)