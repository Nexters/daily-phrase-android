package com.silvertown.android.dailyphrase.data.network.di

import android.content.Context
import com.silvertown.android.dailyphrase.data.Constants.BASE_URL
import com.silvertown.android.dailyphrase.data.datastore.datasource.TokenDataSource
import com.silvertown.android.dailyphrase.data.network.AuthAuthenticator
import com.silvertown.android.dailyphrase.data.network.AuthInterceptor
import com.silvertown.android.dailyphrase.data.network.TokenExpirationNotifier
import com.silvertown.android.dailyphrase.data.network.adpater.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenDataSource: TokenDataSource): AuthInterceptor =
        AuthInterceptor(tokenDataSource)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        tokenDataSource: TokenDataSource,
        @ApplicationContext context: Context,
        tokenExpirationNotifier: TokenExpirationNotifier,
    ): AuthAuthenticator =
        AuthAuthenticator(tokenDataSource, context, tokenExpirationNotifier)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())

    @AuthOkHttpClient
    @Singleton
    @Provides
    fun provideAuthOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @DefaultOkHttpClient
    @Singleton
    @Provides
    fun provideDefaultOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
