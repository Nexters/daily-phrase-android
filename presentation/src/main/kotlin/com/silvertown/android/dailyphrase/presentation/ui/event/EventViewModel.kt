package com.silvertown.android.dailyphrase.presentation.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.model.PrizeInfoUi
import com.silvertown.android.dailyphrase.presentation.model.toPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var timeJob: Job? = null

    init {
        getMockData()
    }

    private fun getMockData() {
        val prizeInfo = PrizeInfo(
            total = 3,
            prizes = listOf(
                PrizeInfo.Prize(
                    prizeId = 1,
                    eventId = 1,
                    name = "무선 전동 마사지건(IMG-300)1232131123",
                    shortName = "test",
                    manufacturer = "인사이디",
                    welcomeImageUrl = "test",
                    bannerImageUrl = "test",
                    imageUrl = "https://image.oliveyoung.co.kr/uploads/images/goods/10/0000/0017/A00000017375314ko.jpg",
                    requiredTicketCount = 5,
                    totalEntryCount = 5,
                    myEntryCount = 1,
                ),
                PrizeInfo.Prize(
                    prizeId = 2,
                    eventId = 2,
                    name = "락토핏 골드 1통(50일분)",
                    shortName = "test",
                    manufacturer = "종근당건강",
                    welcomeImageUrl = "test",
                    bannerImageUrl = "test",
                    imageUrl = "https://image.oliveyoung.co.kr/uploads/images/goods/10/0000/0017/A00000017375314ko.jpg",
                    requiredTicketCount = 4,
                    totalEntryCount = 5,
                    myEntryCount = 2,
                ),
                PrizeInfo.Prize(
                    prizeId = 3,
                    eventId = 3,
                    name = "테스트3",
                    shortName = "test",
                    manufacturer = "종근당3",
                    welcomeImageUrl = "test",
                    bannerImageUrl = "test",
                    imageUrl = "https://image.oliveyoung.co.kr/uploads/images/goods/10/0000/0017/A00000017375314ko.jpg",
                    requiredTicketCount = 3,
                    totalEntryCount = 5,
                    myEntryCount = 3,
                ),
            ),
            eventEndDateTime = "2024-06-23T07:54:15.322Z",
        )

        if (timeJob?.isActive == true) {
            timeJob?.cancel()
        }

        timeJob = viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val givenTime = dateFormat.parse(prizeInfo.eventEndDateTime)

            do {
                val currentTime = Date()
                val durationInMillis = givenTime.time - currentTime.time

                val days = TimeUnit.MILLISECONDS.toDays(durationInMillis)
                val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60

                val formattedHours = String.format("%02d", hours)
                val formattedMinutes = String.format("%02d", minutes)
                val formattedSeconds = String.format("%02d", seconds)

                (if (durationInMillis < 0) {
                    PrizeInfoUi.NoticeInfo.PeriodEnded(
                        textColorResId = R.color.white,
                        bgColorResId = R.color.orange,
                        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1,
                    )
                } else if (days <= 0) {
                    PrizeInfoUi.NoticeInfo.PeriodLessThanOneDay(
                        textColorResId = R.color.white,
                        bgColorResId = R.color.deep_red,
                        formattedTime = "$formattedHours:$formattedMinutes:$formattedSeconds"
                    )
                } else {
                    PrizeInfoUi.NoticeInfo.PeriodMoreThanOneDay(
                        textColorResId = R.color.olive_brown,
                        bgColorResId = R.color.ivory,
                        days = days,
                        formattedTime = "$formattedHours:$formattedMinutes:$formattedSeconds"
                    )
                }).let { noticeInfo ->
                    PrizeInfoUi(
                        total = prizeInfo.total,
                        items = prizeInfo.prizes.map { prize ->
                            prize.toPresentationModel(total = prizeInfo.total, isEventPeriodEnded = durationInMillis < 0)
                        },
                        noticeInfo = noticeInfo
                    )
                }.also { _uiState.emit(UiState.Loaded(it)) }
                delay(1000)
            } while (durationInMillis >= 0)
        }
    }

    sealed interface UiState {
        data object Loading : UiState

        data class Loaded(
            val prizeInfo: PrizeInfoUi,
        ) : UiState
    }
}
