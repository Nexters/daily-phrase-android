package com.silvertown.android.dailyphrase.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _showLoginDialog = MutableStateFlow(false)
    val showLoginDialog = _showLoginDialog.asStateFlow()

    val postList: Flow<PagingData<Post>> =
        postRepository
            .getPosts()
            .cachedIn(viewModelScope)

    val isLoggedIn: StateFlow<Boolean> =
        memberRepository
            .getLoginStateFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1000L),
                initialValue = false
            )

    fun createMember(socialToken: String) {
        // TODO: Api call

        viewModelScope.launch {
            _uiEvent.emit(UiEvent.AlreadySignedUp)
        }
    }

    fun saveBookmark(phraseId: Long) = viewModelScope.launch {
        postRepository
            .saveFavorites(phraseId)
            .onSuccess {
                // TODO: TEST
            }
            .onFailure { errorMessage, code ->
                // TODO: TEST
            }
    }

    fun saveLike(phraseId: Long) = viewModelScope.launch {
        postRepository
            .saveLike(phraseId = phraseId)
            .onSuccess {

            }
            .onFailure { errorMessage, code ->

            }
    }

    fun showLoginDialog(action: Boolean) {
        viewModelScope.launch {
            _showLoginDialog.value = action
        }
    }

    sealed interface UiEvent {
        data object FirstSignup : UiEvent
        data object AlreadySignedUp : UiEvent
    }
}
