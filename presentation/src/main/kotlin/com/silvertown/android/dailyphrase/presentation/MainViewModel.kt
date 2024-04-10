package com.silvertown.android.dailyphrase.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.usecase.GetSignInTokenUseCase
import com.silvertown.android.dailyphrase.presentation.util.Constants.PHRASE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val memberRepository: MemberRepository,
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
) : ViewModel() {

    val phraseId: String? = savedStateHandle.get<String>(PHRASE_ID)

    fun signInWithKaKaoTokenViaServer(
        token: String,
        callback: (Boolean, Long?) -> Unit,
    ) = viewModelScope.launch {
        getSignInTokenUseCase(token)
            .onSuccess {
                memberRepository.saveTokens(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
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

}
