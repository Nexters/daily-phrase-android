package com.silvertown.android.dailyphrase.presentation.model

import com.silvertown.android.dailyphrase.presentation.ui.event.PrizeInfo

data class PrizeInfoUi(
    val total: Int,
    val items: List<Item>,
    val noticeInfo: NoticeInfo,
) {
    data class Item(
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

    sealed class NoticeInfo(
        open val textColorResId: Int,
        open val bgColorResId: Int,
    ) {
        data class PeriodMoreThanOneDay(
            override val textColorResId: Int,
            override val bgColorResId: Int,
            val days: Long,
            val formattedTime: String,
        ) : NoticeInfo(textColorResId, bgColorResId)

        data class PeriodLessThanOneDay(
            override val textColorResId: Int,
            override val bgColorResId: Int,
            val formattedTime: String,
        ) : NoticeInfo(textColorResId, bgColorResId)

        data class PeriodEnded(
            override val textColorResId: Int,
            override val bgColorResId: Int,
            val currentMonth: Int,
        ) : NoticeInfo(textColorResId, bgColorResId)
    }
}

fun PrizeInfo.Prize.toPresentationModel(): PrizeInfoUi.Item {
    return PrizeInfoUi.Item(
        prizeId = prizeId,
        eventId = eventId,
        name = name,
        shortName = shortName,
        manufacturer = manufacturer,
        welcomeImageUrl = welcomeImageUrl,
        bannerImageUrl = bannerImageUrl,
        imageUrl = imageUrl,
        requiredTicketCount = requiredTicketCount,
        totalEntryCount = totalEntryCount,
        myEntryCount = myEntryCount,
    )
}
