package com.silvertown.android.dailyphrase.di

import android.content.Context
import android.content.Intent
import com.silvertown.android.dailyphrase.data.network.TokenExpirationNotifier
import com.silvertown.android.dailyphrase.presentation.MainActivity

class RefreshTokenExpirationNotifier(
    private val context: Context,
) : TokenExpirationNotifier {

    override fun notifyRefreshTokenExpired() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
