package com.silvertown.android.dailyphrase.domain.repository

import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    fun getHomeRewardBanner(): Flow<RewardBanner>
}
