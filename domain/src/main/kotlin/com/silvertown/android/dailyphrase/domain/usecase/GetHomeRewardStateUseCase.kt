package com.silvertown.android.dailyphrase.domain.usecase

import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.domain.model.SharedCountModel
import com.silvertown.android.dailyphrase.domain.repository.MemberRepository
import com.silvertown.android.dailyphrase.domain.repository.RewardRepository
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHomeRewardStateUseCase @Inject constructor(
    private val rewardRepository: RewardRepository,
    private val memberRepository: MemberRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(isLoggedIn: Boolean): Flow<HomeRewardState> {
        val rewardInfoFlow = rewardRepository.getRewardInfo()

        val sharedCountModelFlow = if (isLoggedIn) {
            memberRepository.getSharedCountFlow().map {
                SharedCountModel(it)
            }
        } else {
            flowOf(SharedCountModel(0))
        }

        return sharedCountModelFlow.flatMapLatest { sharedCountModel ->
            val rewardBannerFlow = rewardRepository.getHomeRewardBanner()

            combine(
                rewardBannerFlow,
                rewardInfoFlow,
                flowOf(sharedCountModel)
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
}