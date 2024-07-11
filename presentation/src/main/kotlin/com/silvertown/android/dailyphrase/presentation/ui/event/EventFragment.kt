package com.silvertown.android.dailyphrase.presentation.ui.event

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.BaseFragment
import com.silvertown.android.dailyphrase.presentation.databinding.FragmentEventBinding
import com.silvertown.android.dailyphrase.presentation.extensions.dpToPx
import com.silvertown.android.dailyphrase.presentation.model.PrizeInfoUi
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
        showTicketReceivedDialog() // TODO JH: 조건에 따라 보여주기
    }

    private fun initListeners() {
        binding.vpPrize.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                (viewModel.uiState.value as? EventViewModel.UiState.Loaded)
                    ?.let { it.prizeInfo.items[position] }
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

    private fun updateUi(prizeInfo: PrizeInfoUi) {
        binding.tvMyEntries.text = getString(R.string.my_entries, prizeInfo.total)
        prizeAdapter.setList(prizeInfo.items)
        with(prizeInfo.noticeInfo) {
            binding.tvNotice.setBackgroundColor(resources.getColor(bgColorResId, null))
            binding.tvNotice.setTextColor(resources.getColor(textColorResId, null))
            binding.tvNotice.isSelected = this is PrizeInfoUi.NoticeInfo.PeriodEnded

            when (this) {
                is PrizeInfoUi.NoticeInfo.PeriodEnded -> getString(R.string.event_finish, currentMonth)
                is PrizeInfoUi.NoticeInfo.PeriodLessThanOneDay -> getString(R.string.remaining_entry_period, formattedTime)
                is PrizeInfoUi.NoticeInfo.PeriodMoreThanOneDay -> getString(R.string.remaining_entry_period, "${days}일 $formattedTime")
            }.also { text ->
                binding.tvNotice.text = text
            }
        }
    }

    private fun showTicketReceivedDialog() {
        EventFragmentDirections
            .moveToTicketReceivedFragment()
            .also { findNavController().navigate(it) }
    }
}
