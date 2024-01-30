package com.silvertown.android.dailyphrase.data.network.common

sealed interface ApiResponse<T : Any> {
    class Success<T : Any>(
        val isSuccess: Boolean,
        val code: String,
        val message: String,
        val result: T,
    ) : ApiResponse<T>

    class Error<T : Any>(
        val status: Int,
        val code: String,
        val reason: String,
    ) : ApiResponse<T>

    class Exception<T : Any>(val e: Throwable) : ApiResponse<T>
}

suspend fun <T : Any> ApiResponse<T>.onSuccess(
    action: suspend (T) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Success<T>) {
        action(result)
    }
}

suspend fun <T : Any> ApiResponse<T>.onError(
    action: suspend (status: Int, code: String, reason: String?) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Error<T>) {
        action(status, code, reason)
    }
}

suspend fun <T : Any> ApiResponse<T>.onException(
    action: suspend (e: Throwable) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Exception<T>) {
        action(e)
    }
}
