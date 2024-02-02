package com.silvertown.android.dailyphrase.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.Member
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {

    private val _myPageUiState = MutableStateFlow(MyPageUiState())
    val myPageUiState = _myPageUiState.asStateFlow()

    val memberData: StateFlow<Member> =
        memberRepository
            .memberData
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1000L),
                initialValue = Member()
            )

    fun showLogoutDialog(action: Boolean) {
        viewModelScope.launch {
            _myPageUiState.update { state ->
                state.copy(
                    showLogoutDialog = action
                )
            }
        }
    }

    fun logout() = viewModelScope.launch {
        memberRepository.deleteAccessToken()
    }

}
