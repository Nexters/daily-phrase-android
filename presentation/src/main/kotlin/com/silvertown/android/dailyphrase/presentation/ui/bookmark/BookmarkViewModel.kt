package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _bookmarkList = MutableStateFlow<List<Post>>(emptyList())
    val bookmarkList = _bookmarkList.asStateFlow()

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
}
