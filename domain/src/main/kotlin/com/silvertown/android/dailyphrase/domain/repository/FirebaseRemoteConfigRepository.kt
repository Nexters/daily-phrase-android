package com.silvertown.android.dailyphrase.domain.repository

import kotlinx.coroutines.flow.Flow

interface FirebaseRemoteConfigRepository {
    suspend fun getFirebaseRemoteConfigValue(key: String): Flow<String>

    companion object {
        const val REMOTE_KEY_NEED_UPDATE_APP_VERSION = "need_update_app_version"
        const val REMOTE_KEY_FORCE_UPDATE_APP_VERSION = "force_update_app_version"
    }
}