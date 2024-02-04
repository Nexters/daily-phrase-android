package com.silvertown.android.dailyphrase.presentation.ui.mypage.unsubscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UnsubscribeViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {

    private val _isDeleted = MutableSharedFlow<Boolean>()
    val isDeleted = _isDeleted.asSharedFlow()

    private var _name: String = "user"

    val getName
        get() = _name

    init {
        getMemberName()
    }

    private fun getMemberName() {
        viewModelScope.launch {
            val name = memberRepository.getMemberName()
            if (name.isNotBlank()) {
                _name = memberRepository.getMemberName()
            }
        }
    }

    fun deleteMember() = viewModelScope.launch {
        kakaoLogout()
        memberRepository
            .deleteMember()
            .onSuccess {
                _isDeleted.emit(true)
            }
            .onFailure { errorMessage, code ->
                _isDeleted.emit(false)
                Timber.e("$code $errorMessage")
            }
    }

    private fun kakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Timber.e("로그아웃 실패이지만 SDK에서 토큰 삭제됨 $error")
            } else {
                Timber.e("로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}
