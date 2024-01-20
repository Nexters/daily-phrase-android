package com.silvertown.android.dailyphrase.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.ui.base.BaseFragment
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var adapter: PostAdapter
    val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
        initObserve()
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun initViews() {
        adapter = PostAdapter { moveToPost() }
        binding.rvPost.adapter = adapter
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
                title = "타이틀",
                previewText = "짜잔",
                imageUrl = "임시 데이터입니다.",
                viewCount = 250,
                likeCount = 200,
                isBookmarked = true,
            ),
            Post(
                id = 2,
                title = "타이틀2",
                previewText = "짜잔2",
                imageUrl = "임시 데이터입니다.2",
                viewCount = 130,
                likeCount = 90,
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

    // TODO JH: Domain 모듈에 파일 생성이 잘 안돼서 임시로 여기에 만듬
    data class Post(
        val id: Long,
        val title: String,
        val previewText: String,
        val imageUrl: String,
        val viewCount: Long,
        val likeCount: Long,
        val isBookmarked: Boolean,
    )
}
