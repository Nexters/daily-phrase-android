package com.silvertown.android.dailyphrase.data.network.common

import com.silvertown.android.dailyphrase.domain.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull

const val TIMEOUT_DURATION = 10000L

fun <T : Any> apiRequestFlow(call: suspend () -> ApiResponse<T>): Flow<Result<T>> = flow {
    emit(Result.Loading)

    withTimeoutOrNull(TIMEOUT_DURATION) {
        call().apply {
            onSuccess { data ->
                emit(Result.Success(data))
            }
            onError { code, message ->
                if (code == 204) {
                    emit(Result.Empty)
                } else {
                    val errorMessage = message ?: "Error"
                    emit(Result.Failure(errorMessage, code))
                }
            }
            onException { e ->
                emit(Result.Failure(e.message ?: "Unknown Error", -1))
            }
        }
    } ?: emit(
        Result.Failure(
            "Timeout: ${TIMEOUT_DURATION / 1000} seconds",
            -1
        )
    )
}.flowOn(Dispatchers.IO)
