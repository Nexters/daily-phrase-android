package com.silvertown.android.dailyphrase.data.network.model.response

data class BaseResponse<T>(
    val isSuccess: Boolean?,
    val code: String?,
    val message: String?,
    val result: T?,
    val status: Int?,
    val reason: String?,
)
