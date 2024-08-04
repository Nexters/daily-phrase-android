package com.silvertown.android.dailyphrase.presentation.ui.event

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
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
    private var isItemsSet = false // TODO JH: 개선 방법 고민해보기

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initViews()
        initObserve()
//        showTicketReceivedDialog() // TODO JH: 조건에 따라 보여주기
    }

    private fun initListeners() {
        binding.vpPrize.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                (viewModel.uiState.value as? EventViewModel.UiState.Loaded)?.let {
                    updateEntryUi(
                        prize = it.eventInfo.prizes[position % it.eventInfo.prizes.size],
                        total = it.eventInfo.total,
                    )
                }
            }
        })

        binding.tvSubmitEntries.setOnClickListener {
            (viewModel.uiState.value as? EventViewModel.UiState.Loaded)
                ?.eventInfo
                ?.prizes
                ?.let { prizes -> prizes[binding.vpPrize.currentItem % prizes.size] }
                ?.also { prize ->
                    when (prize) {
                        is EventInfoUi.Prize.AfterWinningDraw -> viewModel.checkEntryResult(selectedPrize = prize)
                        is EventInfoUi.Prize.BeforeWinningDraw -> viewModel.entryEvent(selectedPrize = prize)
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
                        binding.clLoading.isVisible = state is EventViewModel.UiState.Loading
                        if (state is EventViewModel.UiState.Loading) {
                            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        } else {
                            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                        (state as? EventViewModel.UiState.Loaded)?.let { loaded ->
                            updateUi(loaded.eventInfo)
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
                            is EventViewModel.UiEvent.PrizeWinning -> showWinningBottomSheet()
                        }
                    }
            }
        }
    }

    private fun updateUi(eventInfo: EventInfoUi) {
        if (!isItemsSet) {
            prizeAdapter.setList(eventInfo.prizes)
            isItemsSet = true
        }
        updateEntryUi(
            prize = eventInfo.prizes[binding.vpPrize.currentItem % eventInfo.prizes.size],
            total = eventInfo.total,
        )
        with(eventInfo.noticeInfo) {
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

    // 응모와 관련된 ui 업데이트
    private fun updateEntryUi(prize: EventInfoUi.Prize, total: Int) {
        binding.tvSubmitEntries.isEnabled = when (prize) {
            is EventInfoUi.Prize.AfterWinningDraw -> !prize.entryResult.isChecked
            is EventInfoUi.Prize.BeforeWinningDraw -> !prize.isEventPeriodEnded && prize.hasEnoughEntry
        }
        binding.tvMyEntries.text = when (prize) {
            is EventInfoUi.Prize.AfterWinningDraw -> if (prize.entryResult.isChecked) {
                when (prize.entryResult.status) {
                    PrizeInfo.Item.EntryResult.Status.WINNING -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(getString(R.string.entry_result_winning, prize.entryResult.phoneNumber), Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        Html.fromHtml(getString(R.string.entry_result_winning, prize.entryResult.phoneNumber))
                    }

                    PrizeInfo.Item.EntryResult.Status.MISSED -> getString(R.string.entry_result_missed)
                    else -> null
                }
            } else {
                null
            }
            is EventInfoUi.Prize.BeforeWinningDraw -> if (prize.isEventPeriodEnded) {
                null
            } else {
                getString(R.string.my_entries, total)
            }
        }
        binding.tvEntryCount.text = getString(R.string.entry_count_message, prize.myEntryCount)
        binding.tvSubmitEntries.text = if (prize is EventInfoUi.Prize.BeforeWinningDraw) {
            getString(R.string.submit_entries, prize.requiredTicketCount)
        } else {
            getString(R.string.confirm_entry_result)
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
