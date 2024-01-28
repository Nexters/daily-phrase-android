package com.silvertown.android.dailyphrase.data.network

import android.content.Context
import com.silvertown.android.dailyphrase.data.Constants.BASE_URL
import com.silvertown.android.dailyphrase.data.datastore.datasource.TokenDataSource
import com.silvertown.android.dailyphrase.data.network.model.response.SignInTokenResponse
import com.silvertown.android.dailyphrase.data.network.service.SignInApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthAuthenticator @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val context: Context,
    private val tokenExpirationNotifier: TokenExpirationNotifier,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking(Dispatchers.IO) {
            tokenDataSource.getRefreshToken().first()
        }

        return runBlocking(Dispatchers.IO) {
            val newAccessToken = getNewAccessToken(refreshToken)
            if (!newAccessToken.isSuccessful || newAccessToken.body() == null) {
                tokenDataSource.deleteAccessToken()
                tokenExpirationNotifier.notifyRefreshTokenExpired()
                return@runBlocking null
            } else {
                newAccessToken.body()?.let {
                    tokenDataSource.saveAccessToken(it.accessToken ?: "")
                    tokenDataSource.saveRefreshToken(it.refreshToken ?: "")
                    response.request.newBuilder()
                        .header("Authorization", it.accessToken ?: "")
                        .build()
                }
            }
        }
    }

    private suspend fun getNewAccessToken(refreshToken: String?): retrofit2.Response<SignInTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(SignInApiService::class.java)
        return service.replaceToken("$refreshToken")
    }
}
