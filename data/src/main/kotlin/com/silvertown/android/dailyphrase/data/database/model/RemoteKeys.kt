package com.silvertown.android.dailyphrase.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.silvertown.android.dailyphrase.data.database.model.RemoteKeys.Companion.REMOTE_KEYS_TABLE_NAME

@Entity(tableName = REMOTE_KEYS_TABLE_NAME)
data class RemoteKeys(
    @PrimaryKey val id: Long,
    val prevKey: Int?,
    val nextKey: Int?,
) {
    companion object {
        const val REMOTE_KEYS_TABLE_NAME = "remote_keys"
    }
}
