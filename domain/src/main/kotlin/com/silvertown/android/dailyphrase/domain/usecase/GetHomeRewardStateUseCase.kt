package com.silvertown.android.dailyphrase.domain.usecase

import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.domain.repository.RewardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeRewardStateUseCase @Inject constructor(
    private val rewardRepository: RewardRepository,
) {
    operator fun invoke(): Flow<HomeRewardState> {
        val rewardBannerFlow = rewardRepository.getHomeRewardBanner()
        val rewardInfoFlow = rewardRepository.getRewardInfo()

        return combine(rewardBannerFlow, rewardInfoFlow) { banner, info ->
            HomeRewardState(
                rewardBanner = banner,
                name = info.name,
                eventEndDateTime = info.eventEndDateTime,
                shareCount = 10
            )
        }
    }
}
