package com.silvertown.android.dailyphrase.domain.usecase

import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.domain.model.SharedCountModel
import com.silvertown.android.dailyphrase.domain.repository.RewardRepository
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetHomeRewardStateUseCase @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val shareRepository: ShareRepository,
) {
    operator fun invoke(isLoggedIn: Boolean): Flow<HomeRewardState> {
        val rewardBannerFlow = rewardRepository.getHomeRewardBanner()
        val rewardInfoFlow = rewardRepository.getRewardInfo()

        val sharedCountModelFlow = if (isLoggedIn) {
            shareRepository.getSharedCount()
        } else {
            flowOf(SharedCountModel(0))
        }

        return combine(
            rewardBannerFlow,
            rewardInfoFlow,
            sharedCountModelFlow
        ) { banner, info, countModel ->
            HomeRewardState(
                rewardBanner = banner,
                name = info.name,
                eventEndDateTime = info.eventEndDateTime,
                shareCount = countModel.sharedCount
            )
        }
    }
}
