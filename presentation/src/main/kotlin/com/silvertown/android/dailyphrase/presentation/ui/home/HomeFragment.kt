package com.silvertown.android.dailyphrase.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.KakaoLoginDialog
import com.silvertown.android.dailyphrase.presentation.ui.ActionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var adapter: PostAdapter
    private val viewModel by viewModels<HomeViewModel>()
    private var isLoggedIn: Boolean = false
    private var actionState: ActionType = ActionType.NONE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
        initObserve()
        initComposeView()
    }

    private fun initListeners() {
        binding.tvBookmark.setOnClickListener {
            HomeFragmentDirections
                .moveToBookmarkFragment()
                .also { findNavController().navigate(it) }
        }

        binding.ivProfile.setOnClickListener {
            if (isLoggedIn) {
                val action = HomeFragmentDirections.moveToMyPageFragment()
                findNavController().navigate(action)
            } else {
                val action = HomeFragmentDirections.moveToNonLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun initViews() {
        adapter = PostAdapter(
            onPostClick = { moveToDetail(it) },
            onClickBookmark = { phraseId ->
                if (isLoggedIn) {
                    viewModel.saveBookmark(phraseId)
                } else {
                    actionState = ActionType.BOOKMARK
                    viewModel.showLoginDialog(true)
                }
            },
            onClickLike = { phraseId ->
                if (isLoggedIn) {
                    viewModel.saveLike(phraseId)
                } else {
                    actionState = ActionType.LIKE
                    viewModel.showLoginDialog(true)
                }
            }
        )
        binding.rvPost.adapter = adapter
        binding.rvPost.addItemDecoration(PostItemDecoration(requireContext()))
    }

    private fun initObserve() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    Toast.makeText(requireContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoggedIn
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    isLoggedIn = state
                }
        }

    }

    private fun moveToDetail(phraseId: Long) {
        HomeFragmentDirections
            .moveToDetailFragment(phraseId)
            .also { findNavController().navigate(it) }
    }

    private fun kakaoLogin() {
        lifecycleScope.launch {
            kotlin.runCatching {
                loginWithKakaoOrThrow(requireContext())
            }.onSuccess { oAuthToken ->
                UserApiClient.instance.me { user, _ ->
                    if (user != null) {
                        viewModel.createMember(socialToken = oAuthToken.accessToken)
                    }
                }
            }.onFailure { throwable ->
                if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                    Timber.d("사용자가 명시적으로 카카오 로그인 취소")
                } else {
                    Toast.makeText(requireContext(), "로그인에 실패했어요.", Toast.LENGTH_SHORT).show()
                    Timber.e(throwable, "로그인 실패 : ${throwable.message}")
                }
            }
        }
    }

    private suspend fun loginWithKakaoOrThrow(context: Context): OAuthToken {
        return if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            try {
                loginWithKakaoTalk(context)
            } catch (error: Throwable) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) throw error

                loginWithKakaoAccount(context)
            }
        } else {
            loginWithKakaoAccount(context)
        }
    }

    private suspend fun loginWithKakaoTalk(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    error != null -> {
                        continuation.resumeWithException(error)
                    }

                    token != null -> {
                        continuation.resume(token)
                    }

                    else -> {
                        continuation.resumeWithException(RuntimeException("kakao access token 받기 실패"))
                    }
                }
            }
        }
    }

    private suspend fun loginWithKakaoAccount(context: Context): OAuthToken {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                when {
                    error != null -> {
                        continuation.resumeWithException(error)
                    }

                    token != null -> {
                        continuation.resume(token)
                    }

                    else -> {
                        continuation.resumeWithException(RuntimeException("kakao access token 받기 실패"))
                    }
                }
            }
        }
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            val showDialog by viewModel.showLoginDialog.collectAsStateWithLifecycle()

            val messageRes = when (ActionType.valueOf(actionState.name)) {
                ActionType.LIKE -> R.string.login_and_like_message
                ActionType.BOOKMARK -> R.string.login_and_bookmark_message
                ActionType.SHARE -> R.string.login_and_share_message
                ActionType.NONE -> R.string.login_and_share_message
            }

            if (showDialog) {
                BaseDialog(
                    modifier = Modifier,
                    onDismissRequest = {
                        viewModel.showLoginDialog(false)
                    }
                ) {
                    KakaoLoginDialog(
                        message = messageRes,
                        onClickKaKaoLogin = {

                        },
                        onDismissRequest = {
                            viewModel.showLoginDialog(false)
                        }
                    )
                }
            }
        }
    }
}
