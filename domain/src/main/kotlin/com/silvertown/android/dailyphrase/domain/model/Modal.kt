package com.silvertown.android.dailyphrase.domain.model

data class Modal(
    val id: Long,
    val type: String,
    val imageUrl: String,
    val leftButtonMessage: String?,
    val rightButtonMessage: String?,
)
