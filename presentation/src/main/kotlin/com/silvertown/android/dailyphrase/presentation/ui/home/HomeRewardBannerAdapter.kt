package com.silvertown.android.dailyphrase.presentation.ui.home

import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.presentation.ui.reward.HomeRewardBanner

class HomeRewardBannerAdapter(
    private val onClickKaKaoLogin: () -> Unit,
) : ListAdapter<RewardBanner, RewardBannerViewHolder>(RewardBannerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardBannerViewHolder {
        return RewardBannerViewHolder(
            ComposeView(parent.context),
            onClickKaKaoLogin
        )
    }

    override fun onBindViewHolder(holder: RewardBannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RewardBannerDiffCallback : DiffUtil.ItemCallback<RewardBanner>() {
        override fun areItemsTheSame(oldItem: RewardBanner, newItem: RewardBanner): Boolean {
            return oldItem.prizeId == newItem.prizeId
        }

        override fun areContentsTheSame(oldItem: RewardBanner, newItem: RewardBanner): Boolean {
            return oldItem == newItem
        }
    }
}

class RewardBannerViewHolder(
    private val composeView: ComposeView,
    private val onClickKaKaoLogin: () -> Unit,
) : RecyclerView.ViewHolder(composeView) {

    fun bind(rewardBanner: RewardBanner) {
        composeView.setContent {
            HomeRewardBanner(
                modifier = Modifier
                    .padding(top = 19.dp, bottom = 32.dp)
                    .padding(horizontal = 16.dp),
                rewardBanner = rewardBanner,
                onClickKaKaoLogin = onClickKaKaoLogin
            )
        }
    }
}
