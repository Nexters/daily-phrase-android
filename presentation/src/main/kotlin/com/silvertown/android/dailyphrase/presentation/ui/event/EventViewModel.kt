package com.silvertown.android.dailyphrase.presentation.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

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

        viewModelScope.launch {
            _uiState.emit(UiState.Loaded(prizeInfo))
        }
    }

    sealed interface UiState {
        data object Loading : UiState

        data class Loaded(
            val prizeInfo: PrizeInfo,
        ) : UiState
    }
}
