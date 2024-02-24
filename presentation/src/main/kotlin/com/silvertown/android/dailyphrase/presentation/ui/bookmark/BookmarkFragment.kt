package com.silvertown.android.dailyphrase.presentation.ui.bookmark

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentBookmarkBinding
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.ui.home.PostItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>(FragmentBookmarkBinding::inflate) {
    private lateinit var adapter: BookmarkAdapter
    private val viewModel by viewModels<BookmarkViewModel>()
    private var isLoggedIn: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
        initObserve()
    }

    private fun initListeners() {
        binding.tvAllPhrase.setOnClickListener {
            findNavController().popBackStack() // TODO: 주환 Dev 수정 예정
        }
        binding.ivProfile.setOnClickListener {
            if (isLoggedIn) {
                BookmarkFragmentDirections.moveToMyPageFragment()
                    .also { findNavController().navigate(it) }
            } else {
                BookmarkFragmentDirections.moveToNonLoginFragment()
                    .also { findNavController().navigate(it) }
            }
        }
    }

    private fun initViews() {
        adapter = BookmarkAdapter(
            onPostClick = { moveToDetail(it) },
            onClickBookmark = { phraseId ->
                viewModel.deleteBookmark(phraseId)
            },
            onClickLike = { phraseId, state ->
                viewModel.onClickLike(phraseId, state)
            }
        )
        binding.rvPost.adapter = adapter
        binding.rvPost.addItemDecoration(PostItemDecoration(requireContext()))
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookmarkList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    adapter.submitList(it) {
                        val isEmpty = it.isEmpty()
                        with(binding) {
                            rvPost.visibility = if (isEmpty) View.GONE else View.VISIBLE
                            emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
                        }
                    }
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
        BookmarkFragmentDirections
            .moveToDetailFragment(phraseId)
            .also { findNavController().navigate(it) }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.home_app_bar)
        viewModel.getBookmarks()
    }

    override fun onStop() {
        super.onStop()
        setStatusBarColor(R.color.white)
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int) {
        activity?.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(requireContext(), colorRes)
        }
    }
}
