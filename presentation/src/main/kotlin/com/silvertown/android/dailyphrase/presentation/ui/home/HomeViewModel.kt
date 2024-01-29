package com.silvertown.android.dailyphrase.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    postRepository: PostRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val postList: Flow<PagingData<Post>> =
        postRepository.getPosts().cachedIn(viewModelScope)

    fun createMember(socialToken: String) {
        // TODO: Api call

        viewModelScope.launch {
            _uiEvent.emit(UiEvent.AlreadySignedUp)
        }
    }

    sealed interface UiEvent {
        data object FirstSignup : UiEvent
        data object AlreadySignedUp : UiEvent
    }
}
