package com.silvertown.android.dailyphrase.presentation.ui.event

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
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
import com.silvertown.android.dailyphrase.presentation.model.EventInfoUi
import com.silvertown.android.dailyphrase.presentation.util.vibrateSingle
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
                    ?.let { it.prizeInfo.items[position % it.prizeInfo.items.size] }
                    ?.let { prize ->
                        binding.tvEntryCount.text = getString(R.string.entry_count_message, prize.myEntryCount)
                        binding.tvSubmitEntries.text = if (prize is EventInfoUi.Item.BeforeWinningDraw) {
                            getString(R.string.submit_entries, prize.requiredTicketCount)
                        } else {
                            getString(R.string.confirm_entry_result)
                        }
                        binding.tvSubmitEntries.isEnabled = prize.hasEnoughEntry
                    }
            }
        })

        binding.tvSubmitEntries.setOnClickListener {
            (viewModel.uiState.value as? EventViewModel.UiState.Loaded)
                ?.prizeInfo
                ?.items
                ?.let { items -> items[binding.vpPrize.currentItem % items.size] }
                ?.also { item ->
                    when (item) {
                        is EventInfoUi.Item.AfterWinningDraw -> viewModel.checkEntryResult()
                        is EventInfoUi.Item.BeforeWinningDraw -> viewModel.entryEvent(selectedItem = item)
                    }
                }
        }

        setFragmentResultListener(WinningBottomSheet.REQUEST_KEY_ENTERED_PHONE_NUMBER) { _, bundle ->
            bundle.getString(WinningBottomSheet.BUNDLE_KEY_PHONE_NUMBER)?.let { phoneNumber ->
                viewModel.enterPhoneNumber(phoneNumber)
            }
        }
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
            setCurrentItem(Int.MAX_VALUE / 2, true)
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
            launch {
                viewModel.uiState
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collectLatest { state ->
                        (state as? EventViewModel.UiState.Loaded)?.let { loaded ->
                            updateUi(loaded.prizeInfo)
                        }
                    }
            }
            launch {
                viewModel.uiEvent
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collectLatest { event ->
                        when (event) {
                            EventViewModel.UiEvent.EntrySuccess -> {
                                binding.lottieParticle.playAnimation()
                                vibrateSingle(requireContext())
                            }
                        }
                    }
            }
        }
    }

    private fun updateUi(prizeInfo: EventInfoUi) {
        binding.tvMyEntries.text = getString(R.string.my_entries, prizeInfo.total)
        prizeAdapter.setList(prizeInfo.items)
        binding.tvSubmitEntries.isEnabled = prizeInfo.items[binding.vpPrize.currentItem % prizeInfo.items.size].hasEnoughEntry
        with(prizeInfo.noticeInfo) {
            binding.tvNotice.setBackgroundColor(resources.getColor(bgColorResId, null))
            binding.tvNotice.setTextColor(resources.getColor(textColorResId, null))
            binding.tvNotice.isSelected = this is EventInfoUi.NoticeInfo.PeriodEnded

            when (this) {
                is EventInfoUi.NoticeInfo.PeriodEnded -> getString(R.string.event_finish, currentMonth)
                is EventInfoUi.NoticeInfo.PeriodLessThanOneDay -> getString(R.string.remaining_entry_period, formattedTime)
                is EventInfoUi.NoticeInfo.PeriodMoreThanOneDay -> getString(R.string.remaining_entry_period, "${days}일 $formattedTime")
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

    private fun showWinningBottomSheet() {
        EventFragmentDirections
            .moveToWinningBottomSheet()
            .also { findNavController().navigate(it) }
    }
}
