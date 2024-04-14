package com.silvertown.android.dailyphrase.domain.model

data class ShareEvent(
    val memberId: Long,
    val phraseId: Long,
    val sharedAt: String,
)
