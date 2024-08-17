package com.silvertown.android.dailyphrase.presentation.ui.home

import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.presentation.ui.reward.HomeRewardBanner

class HomeRewardBannerAdapter(
    private val onClickKaKaoLogin: () -> Unit,
    private val canCheckThisMonthRewardResult: () -> Boolean,
    private val navigateToEventPage: () -> Unit,
) : ListAdapter<HomeRewardState, RewardBannerViewHolder>(RewardBannerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardBannerViewHolder {
        return RewardBannerViewHolder(
            ComposeView(parent.context),
            onClickKaKaoLogin,
            canCheckThisMonthRewardResult,
            navigateToEventPage
        )
    }

    override fun onBindViewHolder(holder: RewardBannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RewardBannerDiffCallback : DiffUtil.ItemCallback<HomeRewardState>() {
        override fun areItemsTheSame(oldItem: HomeRewardState, newItem: HomeRewardState): Boolean {
            return oldItem.rewardBanner.prizeId == newItem.rewardBanner.prizeId
        }

        override fun areContentsTheSame(
            oldItem: HomeRewardState,
            newItem: HomeRewardState,
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class RewardBannerViewHolder(
    private val composeView: ComposeView,
    private val onClickKaKaoLogin: () -> Unit,
    private val canCheckThisMonthRewardResult: () -> Boolean,
    private val navigateToEventPage: () -> Unit,
) : RecyclerView.ViewHolder(composeView) {

    fun bind(homeRewardState: HomeRewardState) {
        composeView.setContent {
            HomeRewardBanner(
                modifier = Modifier
                    .padding(top = 19.dp, bottom = 32.dp)
                    .padding(horizontal = 16.dp),
                rewardBanner = homeRewardState.rewardBanner,
                eventMonth = homeRewardState.eventMonth,
                onClickKaKaoLogin = onClickKaKaoLogin,
                canCheckThisMonthRewardResult = canCheckThisMonthRewardResult,
                navigateToEventPage = navigateToEventPage
            )
        }
    }
}
