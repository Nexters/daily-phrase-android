package com.silvertown.android.dailyphrase.domain.repository

import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
import com.silvertown.android.dailyphrase.domain.model.Result
import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.domain.model.RewardInfo
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    fun getHomeRewardBanner(): Flow<RewardBanner>
    suspend fun getPrizeInfo(): Result<PrizeInfo> // TODO JH: 나중에 reward든 prize든 통일을 해야할 것 같음
    fun getRewardInfo(): Flow<RewardInfo>
    suspend fun postEventEnter(prizeId: Int): Result<Unit>
    suspend fun postCheckEntryResult(prizeId: Int): Result<Unit>
}
