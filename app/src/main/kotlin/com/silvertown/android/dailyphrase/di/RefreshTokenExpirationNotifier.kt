package com.silvertown.android.dailyphrase.di

import android.content.Context
import android.content.Intent
import com.silvertown.android.dailyphrase.data.network.TokenExpirationNotifier
import com.silvertown.android.dailyphrase.presentation.MainActivity
import timber.log.Timber

class RefreshTokenExpirationNotifier(
    private val context: Context,
) : TokenExpirationNotifier {

    override fun notifyRefreshTokenExpired() {
        Timber.tag("RefreshTokenExpirationNotifier").d("Refresh token expired")
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}
