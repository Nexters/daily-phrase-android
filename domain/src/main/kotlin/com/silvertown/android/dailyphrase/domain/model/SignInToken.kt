package com.silvertown.android.dailyphrase.domain.model

data class SignInToken(
    val memberId: Long,
    val accessToken: String,
    val refreshToken: String,
)
