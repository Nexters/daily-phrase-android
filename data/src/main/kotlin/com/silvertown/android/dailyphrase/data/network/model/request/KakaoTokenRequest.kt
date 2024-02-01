package com.silvertown.android.dailyphrase.data.network.model.request

import com.silvertown.android.dailyphrase.domain.model.KaKaoToken

data class KakaoTokenRequest(
    val identityToken: String,
)

fun KaKaoToken.toRequestModel(): KakaoTokenRequest {
    return KakaoTokenRequest(
        identityToken = this.token
    )
}
