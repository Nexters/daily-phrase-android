package com.silvertown.android.dailyphrase.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _detailUiState =
        MutableStateFlow(
            DetailUiState(phraseId = savedStateHandle["phraseId"] ?: -1)
        )
    val detailUiState = _detailUiState.asStateFlow()

    init {
        getPosts()
        updateLoginState()
    }

    private fun getPosts() {
        viewModelScope.launch {
            postRepository
                .getPost(detailUiState.value.phraseId)
                .onSuccess {
                    _detailUiState.update { state ->
                        state.copy(
                            phraseId = it.phraseId,
                            title = it.title,
                            content = it.content,
                            imageUrl = it.imageUrl,
                            likeCount = it.likeCount,
                            viewCount = it.viewCount,
                            isLike = it.isLike,
                            isBookmark = it.isFavorite
                        )
                    }
                    /** 로컬 싱크 **/
                    postRepository.updateCounts(
                        phraseId = it.phraseId,
                        likeCount = it.likeCount,
                        viewCount = it.viewCount
                    )
                }
                .onFailure { errorMessage, code ->
                    Timber.e("$errorMessage, $code")
                }
        }
    }

    fun onClickLike() {
        if (_detailUiState.value.isLike) {
            deleteLike()
        } else {
            saveLike()
        }
    }

    /**
     * TODO: Bookmark, farvorite 메소드명 통일 필요
     */
    fun onClickBookmark() {
        if (_detailUiState.value.isBookmark) {
            deleteBookmark()
        } else {
            saveBookmark()
        }
    }

    private fun saveLike() = viewModelScope.launch {
        if (getLoginState()) {
            updateLikeState(true)

            postRepository
                .saveLike(phraseId = _detailUiState.value.phraseId)
                .onSuccess {
                    postRepository.updateLikeState(
                        it.phraseId,
                        it.isLike,
                        it.likeCount
                    )
                }
                .onFailure { errorMessage, code ->
                    updateLikeState(false)
                    Timber.e(errorMessage, code)
                }
        } else {
            showLoginDialog(true)
        }
    }

    private fun deleteLike() = viewModelScope.launch {
        if (getLoginState()) {
            updateLikeState(false)

            postRepository
                .deleteLike(phraseId = _detailUiState.value.phraseId)
                .onSuccess {
                    postRepository.updateLikeState(
                        it.phraseId,
                        it.isLike,
                        it.likeCount
                    )
                }
                .onFailure { errorMessage, code ->
                    updateLikeState(true)
                    Timber.e("$errorMessage, $code")
                }
        } else {
            showLoginDialog(true)
        }
    }

    private fun saveBookmark() = viewModelScope.launch {
        if (getLoginState()) {
            updateBookmarkState(true)

            postRepository
                .saveFavorites(phraseId = _detailUiState.value.phraseId)
                .onSuccess {
                    postRepository.updateFavoriteState(
                        it.phraseId,
                        it.isFavorite,
                    )
                }
                .onFailure { errorMessage, code ->
                    updateBookmarkState(false)
                    Timber.e("$errorMessage, $code")
                }
        } else {
            showLoginDialog(true)
        }
    }

    private fun deleteBookmark() = viewModelScope.launch {
        if (getLoginState()) {
            updateBookmarkState(false)

            postRepository
                .deleteFavorites(phraseId = _detailUiState.value.phraseId)
                .onSuccess {
                    postRepository.updateFavoriteState(
                        it.phraseId,
                        it.isFavorite,
                    )
                }
                .onFailure { errorMessage, code ->
                    updateBookmarkState(true)
                    Timber.e(errorMessage, code)
                }
        } else {
            showLoginDialog(true)
        }
    }

    private fun getLoginState() = _detailUiState.value.isLoggedIn

    fun updateLoginState() = viewModelScope.launch {
        _detailUiState.update { state ->
            state.copy(
                isLoggedIn = memberRepository.getLoginStatus()
            )
        }
    }

    private fun updateLikeState(
        isLike: Boolean,
    ) {
        _detailUiState.update { state ->
            state.copy(
                isLike = isLike,
            )
        }
    }

    private fun updateBookmarkState(
        isBookmark: Boolean,
    ) {
        _detailUiState.update { state ->
            state.copy(
                isBookmark = isBookmark,
            )
        }
    }

    fun showLoginDialog(action: Boolean) {
        viewModelScope.launch {
            _detailUiState.update { state ->
                state.copy(
                    showLoginDialog = action
                )
            }
        }
    }

}
