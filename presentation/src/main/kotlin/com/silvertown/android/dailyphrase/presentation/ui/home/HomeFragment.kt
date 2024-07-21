package com.silvertown.android.dailyphrase.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.domain.model.Post
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.KakaoLoginDialog
import com.silvertown.android.dailyphrase.presentation.util.ActionType
import com.silvertown.android.dailyphrase.presentation.ui.reward.RewardPopup
import com.silvertown.android.dailyphrase.presentation.util.Constants.TWENTY_FOUR_HOURS_IN_MILLIS
import com.silvertown.android.dailyphrase.presentation.util.Constants.TWO_MINUTES_IN_MILLIS
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener
import com.silvertown.android.dailyphrase.presentation.util.sendKakaoLink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    LoginResultListener {

    private lateinit var postAdapter: PostAdapter
    private lateinit var rewardBannerAdapter: HomeRewardBannerAdapter
    private lateinit var homeAdapter: ConcatAdapter
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
        (activity as MainActivity).setLoginResultListener(this)

        binding.tvBookmark.setOnClickListener {
            if (isLoggedIn) {
                HomeFragmentDirections
                    .moveToBookmarkFragment()
                    .also { findNavController().navigate(it) }
            } else {
                actionState = ActionType.BOOKMARK
                viewModel.showLoginDialog(true)
            }
        }

        binding.ivProfile.setOnClickListener {
            if (isLoggedIn) {
                HomeFragmentDirections
                    .moveToMyPageFragment()
                    .also { findNavController().navigate(it) }
            } else {
                HomeFragmentDirections
                    .moveToNonLoginFragment()
                    .also { findNavController().navigate(it) }
            }
        }
    }

    private fun initViews() {
        postAdapter = PostAdapter(
            onPostClick = { moveToDetail(it) },
            onClickBookmark = { phraseId, state ->
                onClickBookmark(phraseId, state)
            },
            onClickLike = { phraseId, state ->
                onClickLike(phraseId, state)
            },
            onClickShare = { post ->
                onClickShare(post)
            }
        )

        rewardBannerAdapter = HomeRewardBannerAdapter(
            onClickKaKaoLogin = { (activity as? MainActivity)?.kakaoLogin() }
        )

        binding.rvPost.apply {
            homeAdapter = ConcatAdapter(
                rewardBannerAdapter,
                postAdapter.apply {
                    withLoadStateFooter(PostFooterLoadStateAdapter { postAdapter.retry() })
                }
            )
            adapter = homeAdapter
            postAdapter.addOnPagesUpdatedListener {
                if (postAdapter.itemCount > 0 && viewModel.getFirstLoad()) {
                    scrollToPosition(0)
                    viewModel.setFirstLoad()
                }
            }
            setHasFixedSize(true)
        }

        binding.retryButton.setOnClickListener {
            postAdapter.retry()
        }
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest(postAdapter::submitData)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rewardState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .filterNotNull()
                .collectLatest {
                    rewardBannerAdapter.submitList(listOf(it.rewardBanner))
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoggedIn
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    isLoggedIn = state
                    if (state) {
                        removeRewardBannerAdapter()
                    } else {
                        addRewardBannerAdapter()
                    }
                }
        }

        postAdapter.addLoadStateListener { loadState ->
            binding.loadStateLayout.isVisible = loadState.refresh is LoadState.Error
        }
    }

    private fun moveToDetail(phraseId: Long) {
        HomeFragmentDirections
            .moveToDetailFragment(phraseId)
            .also { findNavController().navigate(it) }
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            val showDialog by viewModel.showLoginDialog.collectAsStateWithLifecycle()
            val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
            val rewardState by viewModel.rewardState.collectAsStateWithLifecycle()
            val messageRes = ActionType.valueOf(actionState.name).messageRes

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
                            (activity as? MainActivity)?.kakaoLogin()
                        },
                        onDismissRequest = {
                            viewModel.showLoginDialog(false)
                        }
                    )
                }
            }

            HomeRewardPopup(rewardState, isLoggedIn)
        }
    }

    override fun onLoginSuccess() {
        viewModel.showLoginDialog(false)
        Toast.makeText(requireContext(), R.string.login_success_desc, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.home_app_bar)
    }

    override fun onStop() {
        super.onStop()
        setStatusBarColor(R.color.white)
    }

    private fun addRewardBannerAdapter() {
        if (!homeAdapter.adapters.contains(rewardBannerAdapter)) {
            homeAdapter.addAdapter(0, rewardBannerAdapter)
        }
    }

    private fun removeRewardBannerAdapter() {
        if (homeAdapter.adapters.contains(rewardBannerAdapter)) {
            homeAdapter.removeAdapter(rewardBannerAdapter)
        }
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int) {
        activity?.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(requireContext(), colorRes)
        }
    }

    private fun onClickBookmark(phraseId: Long, state: Boolean) {
        if (isLoggedIn) {
            viewModel.onClickBookmark(phraseId, state)
        } else {
            actionState = ActionType.BOOKMARK
            viewModel.showLoginDialog(true)
        }
    }

    private fun onClickLike(phraseId: Long, state: Boolean) {
        if (isLoggedIn) {
            viewModel.onClickLike(phraseId, state)
        } else {
            actionState = ActionType.LIKE
            viewModel.showLoginDialog(true)
        }
    }

    private fun onClickShare(post: Post) {
        if (isLoggedIn) {
            sendKakaoLink(
                context = requireContext(),
                phraseId = post.phraseId,
                title = post.title,
                description = post.content,
                imageUrl = post.imageUrl,
                likeCount = post.likeCount,
                viewCount = post.viewCount
            ) {
                viewModel.logShareEvent(post.phraseId)
            }
        } else {
            actionState = ActionType.SHARE
            viewModel.showLoginDialog(true)
        }
    }

    private fun shouldShowPopupTooltip(remainTime: Long): Boolean {
        return remainTime in (TWO_MINUTES_IN_MILLIS + 1) until TWENTY_FOUR_HOURS_IN_MILLIS
    }

    @Composable
    private fun HomeRewardPopup(
        rewardState: HomeRewardState?,
        isLoggedIn: Boolean,
    ) {
        var showRewardPopupTooltip by remember { mutableStateOf(false) }

        rewardState?.let { state ->
            val remainTime =
                Duration.between(LocalDateTime.now(), state.eventEndDateTime).toMillis()

            if (shouldShowPopupTooltip(remainTime)) {
                showRewardPopupTooltip = true
            }

            if (isLoggedIn) {
                RewardPopup(
                    state = state,
                    showRewardPopupTooltip = showRewardPopupTooltip,
                    onTimeBelowThreshold = {
                        showRewardPopupTooltip = false
                    }
                )
            }
        }
    }
}
