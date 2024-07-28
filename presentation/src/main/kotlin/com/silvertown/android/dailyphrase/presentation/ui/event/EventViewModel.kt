package com.silvertown.android.dailyphrase.presentation.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.presentation.model.EventInfoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var timeJob: Job? = null

    // TODO JH: 임시로 만들어둔 mock 데이터는 삭제해두었으니 타이머 로직만 살려서 쓸 것
//    private fun getMockData() {
//        if (timeJob?.isActive == true) {
//            timeJob?.cancel()
//        }
//
//        timeJob = viewModelScope.launch {
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
//            val givenTime = dateFormat.parse(prizeInfo.eventEndDateTime)
//
//            do {
//               val currentTime = Date()
//                val durationInMillis = givenTime.time - currentTime.time
//
//                val days = TimeUnit.MILLISECONDS.toDays(durationInMillis)
//                val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis) % 24
//                val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60
//                val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60
//
//                val formattedHours = String.format("%02d", hours)
//                val formattedMinutes = String.format("%02d", minutes)
//                val formattedSeconds = String.format("%02d", seconds)
//
//                (if (durationInMillis < 0) {
//                    PrizeInfoUi.NoticeInfo.PeriodEnded(
//                        textColorResId = R.color.white,
//                        bgColorResId = R.color.orange,
//                        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1,
//                    )
//                } else if (days <= 0) {
//                    PrizeInfoUi.NoticeInfo.PeriodLessThanOneDay(
//                        textColorResId = R.color.white,
//                        bgColorResId = R.color.deep_red,
//                        formattedTime = "$formattedHours:$formattedMinutes:$formattedSeconds"
//                    )
//                } else {
//                    PrizeInfoUi.NoticeInfo.PeriodMoreThanOneDay(
//                        textColorResId = R.color.olive_brown,
//                        bgColorResId = R.color.ivory,
//                        days = days,
//                        formattedTime = "$formattedHours:$formattedMinutes:$formattedSeconds"
//                    )
//                }).let { noticeInfo ->
//                    PrizeInfoUi(
//                        total = prizeInfo.total,
//                        items = prizeInfo.prizes.map { prize ->
//                            prize.toPresentationModel(total = prizeInfo.total, isEventPeriodEnded = durationInMillis < 0)
//                        },
//                        noticeInfo = noticeInfo
//                    )
//                }.also { _uiState.emit(UiState.Loaded(it)) }
//                delay(1000)
//            } while (durationInMillis >= 0)
//        }
//    }

    fun entryEvent(selectedItem: EventInfoUi.Item) {
        viewModelScope.launch {
            delay(1000) // TODO JH: API 호출 딜레이 (테스트 용)

            // TODO JH: PrizeInfo 삭제해서 빌드가 안되므로 임시 주석 
            // 성공일 때
//            (_uiState.value as? UiState.Loaded)?.let { loaded ->
//                val remainingTickets = loaded.prizeInfo.total - selectedItem.requiredTicketCount
//
//                loaded.prizeInfo.items.map { item ->
//                    if (item.prizeId == selectedItem.prizeId && item is PrizeInfoUi.Item.BeforeWinningDraw) {
//                        item.copy(
//                            myEntryCount = item.myEntryCount + 1,
//                            hasEnoughEntry = remainingTickets >= item.requiredTicketCount,
//                        )
//                    } else {
//                        item
//                    }
//                }.let { items ->
//                    loaded.prizeInfo.copy(items = items, total = remainingTickets)
//                }.let {
//                    _uiState.emit(UiState.Loaded(it))
//                }
//            }
//            _uiEvent.emit(UiEvent.EntrySuccess)
        }
    }

    fun checkEntryResult() {
        // TODO JH: 응모 결과 확인
    }

    fun enterPhoneNumber(phoneNumber: String) {
    }

    sealed interface UiState {
        data object Loading : UiState

        data class Loaded(
            val eventInfo: EventInfoUi,
        ) : UiState
    }

    sealed interface UiEvent {
        data object EntrySuccess : UiEvent
    }
}
