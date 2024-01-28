package com.silvertown.android.dailyphrase.data.network

interface TokenExpirationNotifier {
    fun notifyRefreshTokenExpired()
}
