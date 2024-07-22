package com.silvertown.android.dailyphrase.data.network

import com.silvertown.android.dailyphrase.data.datastore.datasource.TokenDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking(Dispatchers.IO) {
            tokenDataSource.getAccessTokenFlow().first()
        }
        val request =
            chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

        return chain.proceed(request)
    }
}
