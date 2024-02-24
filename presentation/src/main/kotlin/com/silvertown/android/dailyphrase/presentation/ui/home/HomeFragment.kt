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
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentHomeBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.KakaoLoginDialog
import com.silvertown.android.dailyphrase.presentation.ui.ActionType
import com.silvertown.android.dailyphrase.presentation.util.LoginResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    LoginResultListener {

    private lateinit var adapter: PostAdapter
    private val postFooterAdapter = PostFooterLoadStateAdapter { adapter.retry() }
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
        adapter = PostAdapter(
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
        binding.rvPost.adapter = adapter.withLoadStateFooter(postFooterAdapter)
        binding.rvPost.addItemDecoration(PostItemDecoration(requireContext()))
        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    private fun initObserve() {
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

        adapter.addLoadStateListener { loadState ->
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

    override fun onPause() {
        super.onPause()
        setStatusBarColor(R.color.white)
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int) {
        activity?.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(requireContext(), colorRes)
        }
    }

}
