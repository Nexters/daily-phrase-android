package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
) : ViewModel() {

    private val _bookmarkList = MutableStateFlow<List<Post>>(emptyList())
    val bookmarkList = _bookmarkList.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> =
        memberRepository
            .getLoginStateFlow()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(1000L),
                initialValue = false
            )

    init {
        getBookmarks()
    }

    private fun getBookmarks() = viewModelScope.launch {
        postRepository.getFavorites()
            .catch { exception -> Timber.e(exception) }
            .collectLatest { result ->
                result
                    .onSuccess { _bookmarkList.value = it.bookmarkList }
                    .onFailure { errorMessage, code ->
                        Timber.e("$errorMessage $code")
                    }
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

    fun deleteBookmark(phraseId: Long) = viewModelScope.launch {
        postRepository
            .deleteFavorites(phraseId = phraseId)
            .onSuccess {
                getBookmarks()
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
                getBookmarks()
                postRepository.updateLikeState(phraseId, true, it.likeCount)
            }
            .onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
            }
    }

    private fun deleteLike(phraseId: Long) = viewModelScope.launch {
        postRepository
            .deleteLike(phraseId = phraseId)
            .onSuccess {
                getBookmarks()
                postRepository.updateLikeState(phraseId, false, it.likeCount)
            }
            .onFailure { errorMessage, code ->
                Timber.e(errorMessage, code)
            }
    }
}
