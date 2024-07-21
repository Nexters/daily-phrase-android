package com.silvertown.android.dailyphrase.domain.repository

interface ShareRepository {
    suspend fun logShareEvent(
        phraseId: Long,
    )

    suspend fun updateSharedCount()
}
