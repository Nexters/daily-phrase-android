package com.silvertown.android.dailyphrase.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.silvertown.android.dailyphrase.data.database.model.RemoteKeys
import com.silvertown.android.dailyphrase.data.database.model.RemoteKeys.Companion.REMOTE_KEYS_TABLE_NAME

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeysList: List<RemoteKeys>)

    @Query("SELECT * FROM $REMOTE_KEYS_TABLE_NAME WHERE id = :id")
    suspend fun getRemoteKeys(id: Long): RemoteKeys?

    @Query("DELETE FROM $REMOTE_KEYS_TABLE_NAME")
    suspend fun clearRemoteKeys()
}
