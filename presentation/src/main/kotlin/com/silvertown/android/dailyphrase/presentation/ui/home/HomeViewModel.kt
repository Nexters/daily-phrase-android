package com.silvertown.android.dailyphrase.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import com.silvertown.android.dailyphrase.domain.usecase.GetHomeRewardStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
    private val getHomeRewardStateUseCase: GetHomeRewardStateUseCase,
    private val shareRepository: ShareRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _showLoginDialog = MutableStateFlow(false)
    val showLoginDialog = _showLoginDialog.asStateFlow()

    private val _shareEvent = MutableSharedFlow<Unit>()
    val shareEvent = _shareEvent.asSharedFlow()

    private val prevSharedCount: Int?
        get() = savedStateHandle.get<Int>(SHARED_COUNT)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val rewardState: StateFlow<HomeRewardState?> =
        isLoggedIn.flatMapLatest { isLoggedIn ->
            getHomeRewardStateUseCase(isLoggedIn)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000L),
            initialValue = null
        )

    /** TODO: 주환 작업부 **/
    fun createMember(socialToken: String) {
        // TODO: Api call

        viewModelScope.launch {
            _uiEvent.emit(UiEvent.AlreadySignedUp)
        }
    }

    fun onClickLike(
        phraseId: Long,
        state: Boolean,
    ) {
        if (state) {
            deleteLike(phraseId)
        } else {
            saveLike(phraseId)
        }
    }

    fun onClickBookmark(
        phraseId: Long,
        state: Boolean,
    ) {
        if (state) {
            deleteBookmark(phraseId)
        } else {
            saveBookmark(phraseId)
        }
    }

    private fun saveBookmark(phraseId: Long) = viewModelScope.launch {
        postRepository
            .saveFavorites(phraseId = phraseId)
            .onSuccess {
                postRepository.updateFavoriteState(phraseId, true)
            }
            .onFailure { errorMessage, code ->
                Timber.e("$errorMessage, $code")
            }
    }

    private fun deleteBookmark(phraseId: Long) = viewModelScope.launch {
        postRepository
            .deleteFavorites(phraseId = phraseId)
            .onSuccess {
                postRepository.updateFavoriteState(phraseId, false)
            }
            .onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
            }
    }

    private fun saveLike(phraseId: Long) = viewModelScope.launch {
        postRepository
            .saveLike(phraseId = phraseId)
            .onSuccess {
                postRepository.updateLikeState(phraseId, true, it.likeCount)
            }
            .onFailure { errorMessage, code ->
                Timber.e("$errorMessage, $code")
            }
    }

    private fun deleteLike(phraseId: Long) = viewModelScope.launch {
        postRepository
            .deleteLike(phraseId = phraseId)
            .onSuccess {
                postRepository.updateLikeState(phraseId, false, it.likeCount)
            }
            .onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
            }
    }

    fun getFirstLoad(): Boolean {
        return savedStateHandle["key"] ?: false
    }

    fun setFirstLoad() {
        savedStateHandle["key"] = true
    }

    fun showLoginDialog(action: Boolean) {
        viewModelScope.launch {
            _showLoginDialog.value = action
        }
    }

    fun logShareEvent(phraseId: Long) {
        viewModelScope.launch {
            shareRepository.logShareEvent(phraseId)
        }
    }

    private fun updateSharedCount() {
        viewModelScope.launch {
            if (isLoggedIn.value) {
                shareRepository.updateSharedCount()
            }
        }
    }

    fun setPrevSharedCount() {
        viewModelScope.launch {
            rewardState.value?.let {
                savedStateHandle[SHARED_COUNT] = it.shareCount
            }
        }
    }

    fun checkAndEmitSharedEvent() {
        viewModelScope.launch {
            updateSharedCount()

            rewardState.value?.let {
                if (shouldEmitSharedEvent(it.shareCount)) {
                    emitSharedEvent()
                    updatePrevSharedCount(it.shareCount)
                }
            }
        }
    }

    private fun shouldEmitSharedEvent(currentSharedCount: Int): Boolean {
        return (prevSharedCount ?: 0) < currentSharedCount
    }

    private suspend fun emitSharedEvent() {
        _shareEvent.emit(Unit)
    }

    private fun updatePrevSharedCount(currentSharedCount: Int) {
        savedStateHandle[SHARED_COUNT] = currentSharedCount
    }

    sealed interface UiEvent {
        data object FirstSignup : UiEvent
        data object AlreadySignedUp : UiEvent
    }

    companion object {
        const val SHARED_COUNT = "shared_count"
    }
}
