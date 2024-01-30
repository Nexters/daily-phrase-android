package com.silvertown.android.dailyphrase.data.network.common

import com.silvertown.android.dailyphrase.domain.model.Result

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

suspend fun <T : Any, R> ApiResponse<T>.toResultModel(transform: suspend (T) -> R): Result<R> {
    return when (this) {
        is ApiResponse.Success -> Result.Success(transform(result))
        is ApiResponse.Error -> Result.Failure(reason, status)
        is ApiResponse.Exception -> Result.Failure("Api Exception", -1)
    }
}