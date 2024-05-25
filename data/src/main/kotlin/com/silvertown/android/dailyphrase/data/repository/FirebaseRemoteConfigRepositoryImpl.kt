package com.silvertown.android.dailyphrase.data.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRemoteConfigRepositoryImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : FirebaseRemoteConfigRepository {

    override fun getFirebaseRemoteConfigValue(key: String): Flow<String> =
        flow {
            suspendCoroutine { continuation ->
                firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(value = firebaseRemoteConfig[key])
                    } else {
                        continuation.resume(value = null)
                    }
                }
            }.let { remoteConfigValue ->
                remoteConfigValue?.asString().orEmpty()
            }.also { version ->
                emit(version)
            }
        }
}
