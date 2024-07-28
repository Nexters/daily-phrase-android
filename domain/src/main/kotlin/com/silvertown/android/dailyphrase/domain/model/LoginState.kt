package com.silvertown.android.dailyphrase.domain.model

data class LoginState(
    val isLoggedIn: Boolean = false,
    val accessToken: String = "",
)