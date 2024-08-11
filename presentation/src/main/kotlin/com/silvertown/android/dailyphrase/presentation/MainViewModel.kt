package com.silvertown.android.dailyphrase.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.LoginState
import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository.Companion.REMOTE_KEY_FORCE_UPDATE_APP_VERSION
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository.Companion.REMOTE_KEY_NEED_UPDATE_APP_VERSION
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.ModalRepository
import com.silvertown.android.dailyphrase.domain.repository.RewardRepository
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import com.silvertown.android.dailyphrase.domain.usecase.CompareVersionUseCase
import com.silvertown.android.dailyphrase.domain.usecase.GetSignInTokenUseCase
import com.silvertown.android.dailyphrase.presentation.util.Constants.PHRASE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val memberRepository: MemberRepository,
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
    private val firebaseRemoteConfigRepository: FirebaseRemoteConfigRepository,
    private val compareVersionUseCase: CompareVersionUseCase,
    private val modalRepository: ModalRepository,
    private val shareRepository: ShareRepository,
    private val rewardRepository: RewardRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val shouldShowWelcomeModal: Boolean
        get() = savedStateHandle.get<Boolean>(WELCOME_SHOWN_KEY) ?: true

    val loginState: StateFlow<LoginState> =
        memberRepository
            .getLoginStateFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1000L),
                initialValue = LoginState()
            )

    private val _prizeInfo = MutableStateFlow<Result<PrizeInfo>>(Result.Loading)

    val welcomeEventState: Flow<Pair<Result<PrizeInfo>, Boolean>> = combine(
        _prizeInfo,
        rewardRepository.getRewardInfo()
    ) { welcomeEventState, rewardInfo ->
        val currentTime = LocalDateTime.now()
        val isThisMonthRewardClosed =
            Duration.between(currentTime, rewardInfo.eventEndDateTime).isNegative

        Pair(welcomeEventState, isThisMonthRewardClosed)
    }

    init {
        checkVersion()
        fetchPrizeInfo()
    }

    private fun checkVersion() {
        viewModelScope.launch {
            firebaseRemoteConfigRepository.getFirebaseRemoteConfigValue(
                REMOTE_KEY_NEED_UPDATE_APP_VERSION
            )
                .zip(
                    firebaseRemoteConfigRepository.getFirebaseRemoteConfigValue(
                        REMOTE_KEY_FORCE_UPDATE_APP_VERSION
                    )
                ) { n, f ->
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

    fun updateWelcomeModalShown() {
        savedStateHandle[WELCOME_SHOWN_KEY] = false
    }

    private fun fetchPrizeInfo() {
        viewModelScope.launch {
            rewardRepository
                .getPrizeInfo()
                .onSuccess {
                    if (it.total == 0) {
                        _prizeInfo.emit(Result.Empty)
                    } else {
                        _prizeInfo.emit(Result.Success(it))
                    }
                }
                .onFailure { errorMessage, code ->
                    _prizeInfo.emit(Result.Failure(errorMessage, code))
                }
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

    companion object {
        private const val WELCOME_SHOWN_KEY = "welcome_shown"
    }
}
