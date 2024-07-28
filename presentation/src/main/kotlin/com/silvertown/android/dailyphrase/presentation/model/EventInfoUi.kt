package com.silvertown.android.dailyphrase.presentation.model

data class EventInfoUi(
    val total: Int,
    val prizes: List<Prize>,
    val noticeInfo: NoticeInfo,
    val winnerAnnouncementDateTime: String,
) {
    sealed class Prize(
        open val prizeId: Long,
        open val eventId: Long,
        open val name: String,
        open val imageUrl: String,
        open val requiredTicketCount: Int,
        open val totalEntryCount: Int,
        open val myEntryCount: Int,
        open val hasEnoughEntry: Boolean,
    ) {
        data class BeforeWinningDraw(
            override val prizeId: Long,
            override val eventId: Long,
            override val name: String,
            val shortName: String,
            val manufacturer: String,
            val welcomeImageUrl: String,
            val bannerImageUrl: String,
            override val imageUrl: String,
            override val requiredTicketCount: Int,
            override val totalEntryCount: Int,
            override val myEntryCount: Int,
            override val hasEnoughEntry: Boolean,
            val isEventPeriodEnded: Boolean,
        ) : Prize(prizeId, eventId, name, imageUrl, requiredTicketCount, totalEntryCount, myEntryCount, hasEnoughEntry)

        data class AfterWinningDraw(
            override val prizeId: Long,
            override val eventId: Long,
            override val name: String,
            override val imageUrl: String,
            override val requiredTicketCount: Int,
            override val totalEntryCount: Int,
            override val myEntryCount: Int,
            override val hasEnoughEntry: Boolean,
        ) : Prize(prizeId, eventId, name, imageUrl, requiredTicketCount, totalEntryCount, myEntryCount, hasEnoughEntry)
    }

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

// TODO JH: PrizeInfo 삭제해서 빌드가 안되므로 임시 주석
// fun PrizeInfo.Prize.toPresentationModel(total: Int, isEventPeriodEnded: Boolean): PrizeInfoUi.Item {
//    val isBeforeWinningDraw = false // TODO JH: api response에 당첨 발표일이 없어서 임시로 사용
//
//    return if (isBeforeWinningDraw) {
//        PrizeInfoUi.Item.BeforeWinningDraw(
//            prizeId = prizeId,
//            eventId = eventId,
//            name = name,
//            shortName = shortName,
//            manufacturer = manufacturer,
//            welcomeImageUrl = welcomeImageUrl,
//            bannerImageUrl = bannerImageUrl,
//            imageUrl = imageUrl,
//            requiredTicketCount = requiredTicketCount,
//            totalEntryCount = totalEntryCount,
//            myEntryCount = myEntryCount,
//            hasEnoughEntry = total >= requiredTicketCount,
//            isEventPeriodEnded = isEventPeriodEnded,
//        )
//    } else {
//        PrizeInfoUi.Item.AfterWinningDraw(
//            prizeId = prizeId,
//            eventId = eventId,
//            name = name,
//            imageUrl = imageUrl,
//            requiredTicketCount = requiredTicketCount,
//            totalEntryCount = totalEntryCount,
//            myEntryCount = myEntryCount,
//            hasEnoughEntry = total >= requiredTicketCount,
//        )
//    }
// }
