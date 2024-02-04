package com.silvertown.android.dailyphrase.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.usecase.GetSignInTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val memberRepository: MemberRepository,
    private val getSignInTokenUseCase: GetSignInTokenUseCase,
) : ViewModel() {

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
