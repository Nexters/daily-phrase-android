package com.silvertown.android.dailyphrase.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: Long,
    val prevKey: Int?,
    val nextKey: Int?,
)
