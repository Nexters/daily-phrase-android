package com.silvertown.android.dailyphrase.domain.model
data class RewardBanner(
    val eventId: Int,
    val imageUrl: String,
    val manufacturer: String,
    val myEntryCount: Int,
    val name: String,
    val prizeId: Int,
    val requiredTicketCount: Int,
    val shortName: String,
    val totalEntryCount: Int,
)