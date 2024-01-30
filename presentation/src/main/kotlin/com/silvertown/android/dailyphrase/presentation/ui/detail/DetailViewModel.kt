package com.silvertown.android.dailyphrase.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
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
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _detailUiState =
        MutableStateFlow(
            DetailUiState(phraseId = savedStateHandle["phraseId"] ?: -1)
        )
    val detailUiState = _detailUiState.asStateFlow()

    init {
        viewModelScope.launch {
            postRepository
                .getPost(detailUiState.value.phraseId)
                .onSuccess {
                    _detailUiState.update { state ->
                        state.copy(
                            isLike = it.isLike,
                            isBookmark = it.isFavorite
                        )
                    }
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

    private fun saveLike() = viewModelScope.launch {
        updateLikeState(true)

        postRepository
            .saveLike(phraseId = _detailUiState.value.phraseId)
            .onFailure { errorMessage, code ->
                updateLikeState(false)
                Timber.e(errorMessage, code)
            }
    }

    private fun deleteLike() = viewModelScope.launch {
        updateLikeState(false)

        postRepository
            .deleteLike(phraseId = _detailUiState.value.phraseId)
            .onFailure { errorMessage, code ->
                updateLikeState(true)
                Timber.e("$errorMessage, $code")
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

    private fun saveBookmark() = viewModelScope.launch {
        updateBookmarkState(true)

        postRepository
            .saveFavorites(phraseId = _detailUiState.value.phraseId)
            .onFailure { errorMessage, code ->
                updateBookmarkState(false)
                Timber.e("$errorMessage, $code")
            }
    }

    private fun deleteBookmark() = viewModelScope.launch {
        updateBookmarkState(false)

        postRepository
            .deleteFavorites(phraseId = _detailUiState.value.phraseId)
            .onFailure { errorMessage, code ->
                updateBookmarkState(true)
                Timber.e(errorMessage, code)
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
}
