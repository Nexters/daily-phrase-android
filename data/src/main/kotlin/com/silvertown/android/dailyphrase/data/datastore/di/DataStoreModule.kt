package com.silvertown.android.dailyphrase.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.silvertown.android.dailyphrase.data.UserPreferences
import com.silvertown.android.dailyphrase.data.datastore.UserPreferencesSerializer
import com.silvertown.android.dailyphrase.data.datastore.datasource.TokenDataSource
import com.silvertown.android.dailyphrase.data.datastore.datasource.TokenDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    private const val TOKEN_PREFERENCES = "token_pref"

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            corruptionHandler = null
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Singleton
    @Provides
    @Named("TokenPreferences")
    fun provideTokenPreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() },
            ),
            migrations = listOf(SharedPreferencesMigration(context, TOKEN_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(TOKEN_PREFERENCES) },
        )
    }

    @Singleton
    @Provides
    fun provideTokenDataSource(
        @Named("TokenPreferences") tokenPreferencesDataStore: DataStore<Preferences>,
    ): TokenDataSource = TokenDataSourceImpl(tokenPreferencesDataStore)
}
