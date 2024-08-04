package com.silvertown.android.dailyphrase.presentation.util

interface LoginResultListener {
    fun onLoginSuccess()
    fun onLoginSuccess(targetPage: TargetPage) {}
    enum class TargetPage {
        EVENT,
    }
}
