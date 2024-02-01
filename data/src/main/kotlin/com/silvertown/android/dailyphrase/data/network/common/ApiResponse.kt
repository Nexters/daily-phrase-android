package com.silvertown.android.dailyphrase.data.network.common

import com.silvertown.android.dailyphrase.domain.model.Result

sealed interface ApiResponse<T : Any> {
    class Success<T : Any>(
        val data: T,
    ) : ApiResponse<T>

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

suspend fun <T : Any, R> ApiResponse<T>.toResultModel(transform: suspend (T) -> R?): Result<R> {
    return when (this) {
        is ApiResponse.Success -> {
            val transformedResult = runCatching { transform(data) }.getOrNull()
            if (transformedResult != null) {
                Result.Success(transformedResult)
            } else {
                Result.Failure("Transformation resulted in null", -1)
            }
        }

        is ApiResponse.Error -> Result.Failure(message ?: "Error", code)
        is ApiResponse.Exception -> Result.Failure("Api Exception: ${e.message}", -1)
    }
}
