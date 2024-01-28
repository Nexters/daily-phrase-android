package com.silvertown.android.dailyphrase.data.network.model.request

import com.silvertown.android.dailyphrase.domain.model.KaKaoToken

data class KakaoTokenRequest(
    val token: String? = "",
)

fun KaKaoToken.toRequestModel(): KakaoTokenRequest {
    return KakaoTokenRequest(
        token = this.token
    )
}
