package com.silvertown.android.dailyphrase.data.repository

import com.silvertown.android.dailyphrase.data.datastore.datasource.MemberPreferencesDataSource
import com.silvertown.android.dailyphrase.data.network.common.toResultModel
import com.silvertown.android.dailyphrase.data.network.datasource.ShareDataSource
import com.silvertown.android.dailyphrase.data.network.model.request.ShareEventRequest
import com.silvertown.android.dailyphrase.data.network.model.response.toDomainModel
import com.silvertown.android.dailyphrase.domain.model.getOrNull
import com.silvertown.android.dailyphrase.domain.model.onFailure
import com.silvertown.android.dailyphrase.domain.model.onSuccess
import com.silvertown.android.dailyphrase.domain.repository.ShareRepository
import timber.log.Timber
import javax.inject.Inject

class ShareRepositoryImpl @Inject constructor(
    private val shareDataSource: ShareDataSource,
    private val memberPreferencesDataSource: MemberPreferencesDataSource,
) : ShareRepository {
    override suspend fun logShareEvent(phraseId: Long) {
        shareDataSource.logShareEvent(
            data = ShareEventRequest(
                memberId = memberPreferencesDataSource.getMemberId(),
                phraseId = phraseId,
            ),
        ).toResultModel { it.result?.toDomainModel() }
    }

    override suspend fun updateSharedCount(): Int? {
        return try {
            val result = shareDataSource.getSharedCount()
                .toResultModel {
                    it.result?.toDomainModel()
                }

            result.getOrNull()?.let { sharedModel ->
                memberPreferencesDataSource.updateSharedCount(sharedModel.sharedCount)
                sharedModel.sharedCount
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
    }
}
