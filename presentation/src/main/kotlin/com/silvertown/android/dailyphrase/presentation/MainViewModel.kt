package com.silvertown.android.dailyphrase.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.LoginState
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository.Companion.REMOTE_KEY_FORCE_UPDATE_APP_VERSION
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository.Companion.REMOTE_KEY_NEED_UPDATE_APP_VERSION
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.ModalRepository
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import com.silvertown.android.dailyphrase.domain.usecase.CompareVersionUseCase
import com.silvertown.android.dailyphrase.domain.usecase.GetSignInTokenUseCase
import com.silvertown.android.dailyphrase.presentation.util.Constants.PHRASE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val memberRepository: MemberRepository,
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
    private val firebaseRemoteConfigRepository: FirebaseRemoteConfigRepository,
    private val compareVersionUseCase: CompareVersionUseCase,
    private val modalRepository: ModalRepository,
    private val shareRepository: ShareRepository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val loginState: StateFlow<LoginState> =
        memberRepository
            .getLoginStateFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1000L),
                initialValue = LoginState()
            )

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
                        CompareVersionUseCase.UpdateStatus.NEED_UPDATE -> getNeedUpdateModal()
                        CompareVersionUseCase.UpdateStatus.FORCE_UPDATE -> Unit
                    }
                }
        }
    }

    private fun getNeedUpdateModal() {
        viewModelScope.launch {
            modalRepository.getModals().onSuccess { modals ->
                modals
                    .find { it.type == "UPDATE" }
                    ?.let {
                        UiEvent.NeedUpdate(
                            imageUrl = it.imageUrl,
                            leftButtonMessage = it.leftButtonMessage,
                            rightButtonMessage = it.rightButtonMessage,
                        )
                    }
                    ?.also { _uiEvent.emit(it) }
            }.onFailure { errorMessage, code ->
                Timber.e(errorMessage)
            }
        }
    }

    val phraseId: String? = savedStateHandle.get<String>(PHRASE_ID)

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

    fun updateSharedCount() {
        viewModelScope.launch {
            shareRepository.updateSharedCount()
        }
    }

    sealed interface UiEvent {
        data class NeedUpdate(
            val imageUrl: String,
            val leftButtonMessage: String?,
            val rightButtonMessage: String?,
        ) : UiEvent

        data object ForceUpdate : UiEvent
    }
}
