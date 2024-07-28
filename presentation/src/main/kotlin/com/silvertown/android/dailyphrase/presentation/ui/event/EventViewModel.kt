package com.silvertown.android.dailyphrase.presentation.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.model.RewardInfo
import com.silvertown.android.dailyphrase.domain.model.getOrThrow
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.RewardRepository
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.model.EventInfoUi
import com.silvertown.android.dailyphrase.presentation.model.toPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val rewardRepository: RewardRepository,
) : ViewModel() {
    
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    
    private var isTimerActive = true
    private var timeJob: Job? = null
    private val _prizeInfo = MutableStateFlow<Result<PrizeInfo>>(Result.Loading)
    private val _rewardInfo = MutableStateFlow<Result<RewardInfo>>(Result.Loading)
    private val _currentTime = MutableStateFlow<LocalDateTime>(LocalDateTime.now())

    val uiState = combine(
        _prizeInfo,
        _rewardInfo,
        _currentTime,
    ) { prizeInfo, rewardInfo, currentTime ->
        if (prizeInfo is Result.Loading || rewardInfo is Result.Loading) {
            UiState.Loading
        } else if (prizeInfo is Result.Failure) {
            // TODO JH: 실패 케이스 처리
            UiState.Failure
        } else {
            try {
                generateEventInfoUi(
                    prizeInfo.getOrThrow(),
                    rewardInfo.getOrThrow(),
                    currentTime,
                ).let { eventInfoUi ->
                    UiState.Loaded(eventInfoUi)
                }
            } catch (e: Exception) {
                // TODO: Exception 처리
                UiState.Failure
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, UiState.Loading)

    init {
        startTimer()
        fetchPrizeInfo()
        fetchRewardInfo()
    }

    private fun startTimer() {
        if (timeJob?.isActive == true) {
            timeJob?.cancel()
        }

        timeJob = viewModelScope.launch {
            while (isTimerActive) {
                delay(1000)
                _currentTime.emit(LocalDateTime.now())
            }
        }
    }

    private fun stopTimer() {
        isTimerActive = false
    }

    private fun fetchPrizeInfo() {
        viewModelScope.launch {
            rewardRepository
                .getPrizeInfo()
                .onSuccess {
                    _prizeInfo.emit(Result.Success(it))
                }
                .onFailure { errorMessage, code ->
                    _prizeInfo.emit(Result.Failure(errorMessage, code))
                }
        }
    }

    private fun fetchRewardInfo() {
        viewModelScope.launch {
            rewardRepository.getRewardInfo().collect {
                _rewardInfo.emit(Result.Success(it))
            }
        }
    }

    private fun generateEventInfoUi(prizeInfo: PrizeInfo, rewardInfo: RewardInfo, currentTime: LocalDateTime): EventInfoUi {
        val duration = Duration.between(currentTime, rewardInfo.eventEndDateTime)

        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        val seconds = duration.toSeconds() % 60

        val formattedTime = String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds,
        )
        
        if (duration.isNegative) {
            stopTimer()
        }

        return (
            if (duration.isNegative) {
                EventInfoUi.NoticeInfo.PeriodEnded(
                    textColorResId = R.color.white,
                    bgColorResId = R.color.orange,
                    currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1,
                )
            } else if (days <= 0) {
                EventInfoUi.NoticeInfo.PeriodLessThanOneDay(
                    textColorResId = R.color.white,
                    bgColorResId = R.color.deep_red,
                    formattedTime = formattedTime,
                )
            } else {
                EventInfoUi.NoticeInfo.PeriodMoreThanOneDay(
                    textColorResId = R.color.olive_brown,
                    bgColorResId = R.color.ivory,
                    days = days,
                    formattedTime = formattedTime,
                )
            }
            ).let { noticeInfo ->
            val isEventPeriodEnded = duration.isNegative
            val isBeforeWinningDraw = rewardInfo.eventWinnerAnnouncementDateTime?.isAfter(currentTime) ?: true

            EventInfoUi(
                total = prizeInfo.total,
                prizes = prizeInfo.items.map { item ->
                    item.toPresentationModel(
                        hasEnoughEntry = prizeInfo.total >= item.requiredTicketCount,
                        isEventPeriodEnded = isEventPeriodEnded,
                        isBeforeWinningDraw = isBeforeWinningDraw,
                    )
                },
                noticeInfo = noticeInfo,
                winnerAnnouncementDateTime = rewardInfo.eventWinnerAnnouncementDateTime,
            )
        }
    }

    fun entryEvent(selectedPrize: EventInfoUi.Prize) {
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
        data object Failure : UiState

        data class Loaded(
            val eventInfo: EventInfoUi,
        ) : UiState
    }

    sealed interface UiEvent {
        data object EntrySuccess : UiEvent
    }
}
