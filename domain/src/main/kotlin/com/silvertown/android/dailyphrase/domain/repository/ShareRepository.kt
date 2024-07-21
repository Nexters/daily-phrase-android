package com.silvertown.android.dailyphrase.domain.repository

import com.silvertown.android.dailyphrase.domain.model.SharedCountModel
import kotlinx.coroutines.flow.Flow

interface ShareRepository {
    suspend fun logShareEvent(
        phraseId: Long,
    )

    fun getSharedCount(): Flow<SharedCountModel>
}
