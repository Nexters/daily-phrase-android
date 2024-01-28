package com.silvertown.android.dailyphrase.domain.model

data class SignInToken(
    val accessToken: String,
    val refreshToken: String,
)
