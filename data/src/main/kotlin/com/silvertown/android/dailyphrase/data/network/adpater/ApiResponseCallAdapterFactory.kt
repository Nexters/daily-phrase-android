package com.silvertown.android.dailyphrase.data.network.adpater

import com.silvertown.android.dailyphrase.data.network.common.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseCallAdapterFactory private constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            return null
        }
        val callType = getParameterUpperBound(0, returnType)

        if (getRawType(callType) != ApiResponse::class.java) {
            return null
        }

        if (callType !is ParameterizedType) {
            return null
        }
        val resultType = getParameterUpperBound(0, callType)
        return ApiResponseCallAdapter(resultType)
    }

    companion object {
        fun create(): ApiResponseCallAdapterFactory = ApiResponseCallAdapterFactory()
    }
}
