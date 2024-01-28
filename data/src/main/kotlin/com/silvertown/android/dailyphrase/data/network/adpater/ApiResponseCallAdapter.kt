package com.silvertown.android.dailyphrase.data.network.adpater

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class ApiResponseCallAdapter(
    private val responseType: Type,
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCall(call)
    }
}

class ApiResponseCall<T : Any>(
    private val call: Call<T>,
) : Call<ApiResponse<T>> {
    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResponse = handleApi { response }
                callback.onResponse(this@ApiResponseCall, Response.success(apiResponse))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResponse = ApiResponse.Exception<T>(t)
                callback.onResponse(this@ApiResponseCall, Response.success(apiResponse))
            }
        })
    }

    override fun execute(): Response<ApiResponse<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResponse<T>> = ApiResponseCall(call.clone())
    override fun request(): Request = call.request()
    override fun timeout(): Timeout = call.timeout()
    override fun isExecuted(): Boolean = call.isExecuted
    override fun isCanceled(): Boolean = call.isCanceled
    override fun cancel() {
        call.cancel()
    }
}
