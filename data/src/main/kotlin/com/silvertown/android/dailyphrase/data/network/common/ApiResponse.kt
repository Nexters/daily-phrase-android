package com.silvertown.android.dailyphrase.data.network.common

sealed interface ApiResponse<T : Any> {
    class Success<T : Any>(val data: T) : ApiResponse<T>
    class Error<T : Any>(val code: Int, val message: String?) : ApiResponse<T>
    class Exception<T : Any>(val e: Throwable) : ApiResponse<T>
}

suspend fun <T : Any> ApiResponse<T>.onSuccess(
    action: suspend (T) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Success<T>) {
        action(data)
    }
}

suspend fun <T : Any> ApiResponse<T>.onError(
    action: suspend (code: Int, message: String?) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Error<T>) {
        action(code, message)
    }
}

suspend fun <T : Any> ApiResponse<T>.onException(
    action: suspend (e: Throwable) -> Unit,
): ApiResponse<T> = apply {
    if (this is ApiResponse.Exception<T>) {
        action(e)
    }
}
