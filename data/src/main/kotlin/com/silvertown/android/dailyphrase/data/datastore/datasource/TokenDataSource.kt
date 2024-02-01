package com.silvertown.android.dailyphrase.data.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getLoginState(): Boolean
    suspend fun deleteAccessToken()

}
