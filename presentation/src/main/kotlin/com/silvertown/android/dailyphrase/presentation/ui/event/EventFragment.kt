package com.silvertown.android.dailyphrase.presentation.ui.event

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentEventBinding
import com.silvertown.android.dailyphrase.presentation.extensions.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventFragment : BaseFragment<FragmentEventBinding>(FragmentEventBinding::inflate) {
    private lateinit var prizeAdapter: PrizeAdapter
    private val viewModel by viewModels<EventViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
        initObserve()
    }

    private fun initListeners() {
        binding.vpPrize.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                (viewModel.uiState.value as? EventViewModel.UiState.Loaded)
                    ?.let { it.prizeInfo.prizes[position] }
                    ?.let { prize ->
                        binding.tvEntryCount.text = getString(R.string.entry_count_message, prize.myEntryCount)
                        binding.tvSubmitEntries.text = getString(R.string.submit_entries, prize.requiredTicketCount)
                    }
            }
        })
    }

    private fun initViews() {
        val pageMarginPx = 53.dpToPx(requireContext())
        val offsetPx = pageMarginPx / 2

        with(binding.vpPrize) {
            setPageTransformer { page, position ->
                page.translationX = position * -offsetPx
            }

            offscreenPageLimit = 1
            prizeAdapter = PrizeAdapter()
            adapter = prizeAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State,
                ) {
                    outRect.right = offsetPx
                    outRect.left = offsetPx
                }
            })
        }
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    (state as? EventViewModel.UiState.Loaded)?.let { loaded ->
                        updateUi(loaded.prizeInfo)
                    }
                }
        }
    }

    private fun updateUi(prizeInfo: PrizeInfo) {
        binding.tvNotice.text = getString(R.string.remaining_entry_period, prizeInfo.eventEndDateTime)
        binding.tvMyEntries.text = getString(R.string.my_entries, prizeInfo.total)
        prizeAdapter.setList(prizeInfo.prizes)
    }
}
