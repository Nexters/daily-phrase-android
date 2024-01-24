package com.silvertown.android.dailyphrase.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var adapter: PostAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
        initObserve()
    }

    private fun initListeners() {
        binding.tvBookmark.setOnClickListener {
            HomeFragmentDirections
                .moveToBookmarkFragment()
                .also { findNavController().navigate(it) }
        }
    }

    private fun initViews() {
        adapter = PostAdapter { moveToPost() }
        binding.rvPost.adapter = adapter
        binding.rvPost.addItemDecoration(PostItemDecoration(requireContext()))

        adapter.submitList(getData())
    }

    private fun initObserve() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    Toast.makeText(requireContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getData(): List<Post> {
        return listOf(
            Post(
                id = 1,
                title = "자식사랑 내리사랑",
                previewText = "어느날 시계를 보다가 문득 이런 생각을 한 적이 있습니다. 성급한 사람, 무덤덤한 사람, 아무 생각이 없는 사람",
                imageUrl = "https://picsum.photos/800/400",
                viewCount = 1020104,
                likeCount = 9999,
                isBookmarked = true,
            ),
            Post(
                id = 2,
                title = "잔잔해 보이지만",
                previewText = "어느날 시계를 보다가 문득 이런 생각을 한 적이 있습니다. 성급한 사람, 무덤덤한 사람, 아무 생각이 없는 사람",
                imageUrl = "https://picsum.photos/800/400",
                viewCount = 130,
                likeCount = 90,
                isBookmarked = false,
            ),
            Post(
                id = 3,
                title = "사랑의 향기가 나는 시간",
                previewText = "어느날 시계를 보다가 문득 이런 생각을 한 적이 있습니다.",
                imageUrl = null,
                viewCount = 139501,
                likeCount = 1234,
                isBookmarked = true,
            ),
            Post(
                id = 4,
                title = "커피를 많이 마실수록 더 오래산다? - 의학계에서 긍정적인 측면",
                previewText = "오늘의 끝이 내일의 처음입니다. 오늘 무엇을 했느냐가 내일을 결정합니다. 오늘 바쁜 일을 미루면 더 바쁜 내일이 되고, 오늘",
                imageUrl = null,
                viewCount = 12345,
                likeCount = 316,
                isBookmarked = true,
            ),
        )
    }

    private fun moveToPost() {
        HomeFragmentDirections
            .moveToPostFragment()
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
}
