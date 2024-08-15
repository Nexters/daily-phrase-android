package com.silvertown.android.dailyphrase.domain.model

import java.time.LocalDateTime

data class RewardBanner(
    val eventId: Int,
    val welcomeImageUrl: String,
    val bannerImageUrl: String,
    val imageUrl: String,
    val manufacturer: String,
    val myEntryCount: Int,
    val name: String,
    val prizeId: Int,
    val requiredTicketCount: Int,
    val totalParticipantCount: Int,
    val shortName: String,
    val totalEntryCount: Int,
    val myTicketCount: Int,
)

data class HomeRewardState(
    val rewardBanner: RewardBanner,
    val name: String,
    val eventMonth: Int,
    val eventEndDateTime: LocalDateTime?,
    val shareCount: Int,
    val isThisMonthRewardClosed: Boolean,
    val isBeforeWinningDraw: Boolean,
)
