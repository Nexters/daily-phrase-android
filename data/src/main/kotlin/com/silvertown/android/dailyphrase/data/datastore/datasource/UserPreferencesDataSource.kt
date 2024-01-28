package com.silvertown.android.dailyphrase.data.datastore.datasource

import androidx.datastore.core.DataStore
import com.silvertown.android.dailyphrase.data.UserPreferences
import com.silvertown.android.dailyphrase.domain.model.User
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map { preferences ->
            User(
                id = preferences.id,
                nickName = preferences.name,
                profileImage = preferences.profile
            )
        }

    suspend fun setUserName(userName: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setName(userName)
                .build()
        }
    }

    suspend fun setUserProfile(userProfile: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setProfile(userProfile)
                .build()
        }
    }

    suspend fun setUserId(userId: String) {
        userPreferences.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setId(userId)
                .build()
        }
    }
}
