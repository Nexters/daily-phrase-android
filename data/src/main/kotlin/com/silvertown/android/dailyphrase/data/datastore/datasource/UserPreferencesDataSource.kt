package com.silvertown.android.dailyphrase.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.silvertown.android.dailyphrase.data.UserPreferences
import com.silvertown.android.dailyphrase.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map { preferences ->
            User(
                id = preferences.id,
                name = preferences.name,
                imageUrl = preferences.imageUrl
            )
        }

    suspend fun getMemberId(): Long {
        return userPreferences.data
            .map { preferences ->
                preferences.id
            }.firstOrNull() ?: -1
    }

    suspend fun setName(name: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setName(name)
                .build()
        }
    }

    suspend fun setImageUrl(imageUrl: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setImageUrl(imageUrl)
                .build()
        }
    }

    suspend fun setMemberId(memberId: Long) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setId(memberId)
                .build()
        }
    }
}
