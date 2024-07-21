package com.silvertown.android.dailyphrase.data.repository

import com.silvertown.android.dailyphrase.data.network.common.toResultModel
import com.silvertown.android.dailyphrase.data.network.datasource.RewardDataSource
import com.silvertown.android.dailyphrase.data.network.model.response.toDomainModel
import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.domain.model.RewardInfo
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.RewardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class RewardRepositoryImpl @Inject constructor(
    private val rewardDataSource: RewardDataSource,
) : RewardRepository {
    override fun getHomeRewardBanner(): Flow<RewardBanner> = flow {
        rewardDataSource
            .getHomeRewards()
            .toResultModel { rewardWrapper ->
                rewardWrapper.result?.rewardList?.map { reward ->
                    reward.toDomainModel(rewardWrapper.result.eventEndDateTime)
                }
            }
            .onSuccess { rewardBannerList ->
                rewardBannerList.randomOrNull()?.let { rewardBanner ->
                    emit(rewardBanner)
                }
            }
            .onFailure { errorMessage, _ ->
                Timber.e(errorMessage)
            }
    }

    override fun getRewardInfo(): Flow<RewardInfo> = flow {
        rewardDataSource.getRewardInfo()
            .toResultModel {
                it.result?.toDomainModel()
            }
            .onSuccess { rewardInfo ->
                emit(rewardInfo)
            }
            .onFailure { errorMessage, _ ->
                Timber.e(errorMessage)
            }
    }
}
