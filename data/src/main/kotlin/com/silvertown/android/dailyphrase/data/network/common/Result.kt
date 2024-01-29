package com.silvertown.android.dailyphrase.data.network.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Failure(val errorMessage: String, val code: Int) : Result<Nothing>
    object Loading : Result<Nothing>
    object Empty : Result<Nothing>
}

suspend fun <T> Result<T>.onSuccess(
    action: suspend (T) -> Unit,
): Result<T> = apply {
    if (this is Result.Success) {
        action(data)
    }
}

suspend fun Result<*>.onFailure(
    action: suspend (errorMessage: String, code: Int) -> Unit,
): Result<*> =
    apply {
        if (this is Result.Failure) {
            action(errorMessage, code)
        }
    }

suspend fun Result<*>.onEmpty(
    action: suspend () -> Unit,
): Result<*> =
    apply {
        if (this is Result.Empty) {
            action()
        }
    }

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Failure(it.message ?: "Throwable", -1)) }
}

inline fun <reified R, reified D> Flow<Result<R>>.mapResultModel(crossinline transform: (R) -> D): Flow<Result<D>> {
    return this.map { apiResponse ->
        when (apiResponse) {
            is Result.Loading -> Result.Loading
            is Result.Success -> Result.Success(transform(apiResponse.data))
            is Result.Failure -> Result.Failure(apiResponse.errorMessage, apiResponse.code)
            is Result.Empty -> Result.Empty
        }
    }
}

suspend fun <T : Any, R> ApiResponse<T>.toResultModel(transform: suspend (T) -> R): Result<R> {
    return when (this) {
        is ApiResponse.Success -> Result.Success(transform(result))
        is ApiResponse.Error -> Result.Failure(reason, status)
        is ApiResponse.Exception -> Result.Failure("Api Exception", -1)
    }
}
