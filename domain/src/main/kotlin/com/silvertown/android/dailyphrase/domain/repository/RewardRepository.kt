package com.silvertown.android.dailyphrase.domain.repository

import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.domain.model.RewardInfo
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    fun getHomeRewardBanner(): Flow<RewardBanner>
    fun getRewardInfo(): Flow<RewardInfo>
}
