package com.silvertown.android.dailyphrase.data.network.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.silvertown.android.dailyphrase.data.repository.FirebaseRemoteConfigRepositoryImpl
import com.silvertown.android.dailyphrase.domain.repository.FirebaseRemoteConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {
    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }

        return FirebaseRemoteConfig
            .getInstance()
            .apply {
                setConfigSettingsAsync(configSettings)
            }
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfigRepository(
        firebaseRemoteConfig: FirebaseRemoteConfig,
    ): FirebaseRemoteConfigRepository {
        return FirebaseRemoteConfigRepositoryImpl(firebaseRemoteConfig)
    }
}