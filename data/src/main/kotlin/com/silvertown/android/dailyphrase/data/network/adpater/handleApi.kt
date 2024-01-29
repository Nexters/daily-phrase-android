package com.silvertown.android.dailyphrase.data.network.adpater

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

fun <T : Any> handleApi(
    execute: () -> Response<T>,
): ApiResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Timber.tag("network").e("handleApi : response.isSuccessful && body not null")
            ApiResponse.Success(
                isSuccess = response.isSuccessful,
                code = response.code().toString(),
                message = response.message(),
                result = body
            )
        } else {
            Timber.tag("network").e("handleApi : response not Successful or body is null ")
            ApiResponse.Error(
                status = response.code(),
                code = response.code().toString(),
                reason = response.message()
            )
        }
    } catch (e: HttpException) {
        Timber.tag("network")
            .e("handleApi : Error HttpException code : ${e.code()} message : ${e.message}")
        ApiResponse.Error(
            status = e.code(),
            code = e.code().toString(),
            reason = e.message()
        )
    } catch (e: Throwable) {
        Timber.tag("network").e("handleApi : Exception message :  ${e.message}")
        ApiResponse.Exception(e)
    }
}
