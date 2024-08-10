package com.silvertown.android.dailyphrase.presentation.model

import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
import java.time.LocalDateTime

data class EventInfoUi(
    val total: Int,
    val prizes: List<Prize>,
    val noticeInfo: NoticeInfo,
    val winnerAnnouncementDateTime: LocalDateTime?,
) {
    sealed class Prize(
        open val prizeId: Int,
        open val eventId: Int,
        open val name: String,
        open val imageUrl: String,
        open val requiredTicketCount: Int,
        open val totalEntryCount: Int,
        open val myEntryCount: Int,
        open val hasEnoughEntry: Boolean,
        open val entryResult: EntryResult,
    ) {
        data class BeforeWinningDraw(
            override val prizeId: Int,
            override val eventId: Int,
            override val name: String,
            val manufacturer: String,
            override val imageUrl: String,
            override val requiredTicketCount: Int,
            override val totalEntryCount: Int,
            override val myEntryCount: Int,
            override val hasEnoughEntry: Boolean,
            override val entryResult: EntryResult,
            val isEventPeriodEnded: Boolean,
            val winningResultDate: LocalDateTime,
        ) : Prize(prizeId, eventId, name, imageUrl, requiredTicketCount, totalEntryCount, myEntryCount, hasEnoughEntry, entryResult)

        data class AfterWinningDraw(
            override val prizeId: Int,
            override val eventId: Int,
            override val name: String,
            override val imageUrl: String,
            override val requiredTicketCount: Int,
            override val totalEntryCount: Int,
            override val myEntryCount: Int,
            override val hasEnoughEntry: Boolean,
            override val entryResult: EntryResult,
        ) : Prize(prizeId, eventId, name, imageUrl, requiredTicketCount, totalEntryCount, myEntryCount, hasEnoughEntry, entryResult)
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

    data class EntryResult(
        val status: PrizeInfo.Item.EntryResult.Status,
        val phoneNumber: String?,
        val isChecked: Boolean,
    )
}

fun PrizeInfo.Item.toPresentationModel(
    hasEnoughEntry: Boolean,
    isEventPeriodEnded: Boolean,
    isBeforeWinningDraw: Boolean,
    winningResultDate: LocalDateTime,
): EventInfoUi.Prize {
    return if (isBeforeWinningDraw) {
        EventInfoUi.Prize.BeforeWinningDraw(
            prizeId = prizeId,
            eventId = eventId,
            name = name,
            manufacturer = manufacturer,
            imageUrl = imageUrl,
            requiredTicketCount = requiredTicketCount,
            totalEntryCount = totalEntryCount,
            myEntryCount = myEntryCount,
            hasEnoughEntry = hasEnoughEntry,
            isEventPeriodEnded = isEventPeriodEnded,
            entryResult = entryResult.toPresentationModel(),
            winningResultDate = winningResultDate,
        )
    } else {
        EventInfoUi.Prize.AfterWinningDraw(
            prizeId = prizeId,
            eventId = eventId,
            name = name,
            imageUrl = imageUrl,
            requiredTicketCount = requiredTicketCount,
            totalEntryCount = totalEntryCount,
            myEntryCount = myEntryCount,
            hasEnoughEntry = hasEnoughEntry,
            entryResult = entryResult.toPresentationModel()
        )
    }
}

fun PrizeInfo.Item.EntryResult.toPresentationModel(): EventInfoUi.EntryResult {
    return EventInfoUi.EntryResult(
        status = status,
        phoneNumber = phoneNumber,
        isChecked = isChecked,
    )
}