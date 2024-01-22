package com.silvertown.android.dailyphrase.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun createMember(socialToken: String) {
        // TODO: Api call

        viewModelScope.launch {
            _uiEvent.emit(UiEvent.AlreadySignedUp)
        }
    }

    sealed interface UiEvent {
        data object FirstSignup : UiEvent
        data object AlreadySignedUp : UiEvent
    }
}
