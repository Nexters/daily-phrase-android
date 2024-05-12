package com.silvertown.android.dailyphrase.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository.Companion.REMOTE_KEY_FORCE_UPDATE_APP_VERSION
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository.Companion.REMOTE_KEY_NEED_UPDATE_APP_VERSION
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.usecase.CompareVersionUseCase
import com.silvertown.android.dailyphrase.domain.usecase.GetSignInTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val memberRepository: MemberRepository,
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
    private val firebaseRemoteConfigRepository: FirebaseRemoteConfigRepository,
    private val compareVersionUseCase: CompareVersionUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        checkVersion()
    }

    private fun checkVersion() {
        viewModelScope.launch {
            firebaseRemoteConfigRepository.getFirebaseRemoteConfigValue(REMOTE_KEY_NEED_UPDATE_APP_VERSION)
                .zip(firebaseRemoteConfigRepository.getFirebaseRemoteConfigValue(REMOTE_KEY_FORCE_UPDATE_APP_VERSION)) { n, f ->
                    compareVersionUseCase(needUpdateVersion = n, forceUpdateVersion = f)
                }.collect { updateStatus ->
                    when (updateStatus) {
                        CompareVersionUseCase.UpdateStatus.NOT_NEED_UPDATE -> Unit
                        CompareVersionUseCase.UpdateStatus.NEED_UPDATE -> _uiEvent.emit(UiEvent.NeedUpdate)
                        CompareVersionUseCase.UpdateStatus.FORCE_UPDATE -> _uiEvent.emit(UiEvent.ForceUpdate)
                    }
                }
        }
    }

    fun signInWithKaKaoTokenViaServer(
        token: String,
        callback: (Boolean, Long?) -> Unit,
    ) = viewModelScope.launch {
        getSignInTokenUseCase(token)
            .onSuccess {
                memberRepository.saveTokens(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                )
                callback(true, it.memberId)
            }.onFailure { errorMessage, code ->
                Timber.tag("SignInViewModel").e("$code $errorMessage")
                callback(false, null)
            }
    }

    fun setMemberData(
        id: Long?,
        name: String?,
        imageUrl: String?,
    ) {
        viewModelScope.launch {
            if (listOf(id, name, imageUrl).all { it != null }) {
                memberRepository.setMemberData(id!!, name!!, imageUrl!!)
            }
        }
    }

    sealed interface UiEvent {
        data object NeedUpdate : UiEvent
        data object ForceUpdate : UiEvent
    }
}
