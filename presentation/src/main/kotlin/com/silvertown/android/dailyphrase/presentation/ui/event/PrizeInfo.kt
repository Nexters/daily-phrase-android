package com.silvertown.android.dailyphrase.presentation.ui.event

data class PrizeInfo(
    val total: Int,
    val prizes: List<Prize>,
    val eventEndDateTime: String,
) {
    data class Prize(
        val prizeId: Long,
        val eventId: Long,
        val name: String,
        val shortName: String,
        val manufacturer: String,
        val welcomeImageUrl: String,
        val bannerImageUrl: String,
        val imageUrl: String,
        val requiredTicketCount: Int,
        val totalEntryCount: Int,
        val myEntryCount: Int,
    )
}
