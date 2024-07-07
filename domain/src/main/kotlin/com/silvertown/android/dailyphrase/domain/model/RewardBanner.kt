package com.silvertown.android.dailyphrase.domain.model

import java.time.LocalDateTime

data class RewardBanner(
    val eventId: Int,
    val imageUrl: String,
    val manufacturer: String,
    val myEntryCount: Int,
    val name: String,
    val prizeId: Int,
    val requiredTicketCount: Int,
    val totalParticipantCount: Int,
    val shortName: String,
    val totalEntryCount: Int,
    val eventEndDateTime: LocalDateTime?,
)
