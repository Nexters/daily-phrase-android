package com.silvertown.android.dailyphrase.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.silvertown.android.dailyphrase.domain.model.SignInToken

data class SignInTokenResponse(
    @SerializedName("memberId")
    val memberId: Long?,
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
)

fun SignInTokenResponse.toDomainModel() = SignInToken(
    memberId = memberId ?: 0,
    accessToken = accessToken.orEmpty(),
    refreshToken = refreshToken.orEmpty()
)
