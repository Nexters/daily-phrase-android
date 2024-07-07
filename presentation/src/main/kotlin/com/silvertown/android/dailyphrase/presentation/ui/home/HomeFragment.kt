package com.silvertown.android.dailyphrase.presentation.ui.home

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.runtime.getValue
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
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.KakaoLoginDialog
import com.silvertown.android.dailyphrase.presentation.ui.ActionType
import com.silvertown.android.dailyphrase.presentation.ui.reward.RewardPopup
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

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
                if (isLoggedIn) {
                    viewModel.onClickBookmark(phraseId, state)
                } else {
                    actionState = ActionType.BOOKMARK
                    viewModel.showLoginDialog(true)
                }
            },
            onClickLike = { phraseId, state ->
                if (isLoggedIn) {
                    viewModel.onClickLike(phraseId, state)
                } else {
                    actionState = ActionType.LIKE
                    viewModel.showLoginDialog(true)
                }
            }
        )

        rewardBannerAdapter = HomeRewardBannerAdapter(
            onClickKaKaoLogin = { (activity as? MainActivity)?.kakaoLogin() }
        )

        binding.rvPost.apply {
            homeAdapter = ConcatAdapter(
                rewardBannerAdapter,
                postAdapter.run {
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
            addItemDecoration(PostItemDecoration(requireContext()))
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
            viewModel.rewardBanner
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .filterNotNull()
                .collectLatest {
                    rewardBannerAdapter.submitList(listOf(it))
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
            val rewardBanner by viewModel.rewardBanner.collectAsStateWithLifecycle()

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
                            (activity as? MainActivity)?.kakaoLogin()
                        },
                        onDismissRequest = {
                            viewModel.showLoginDialog(false)
                        }
                    )
                }
            }

            if (isLoggedIn && rewardBanner != null) {
                RewardPopup(
                    rewardBanner = rewardBanner!!
                )
            }
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

}
