package com.example.qiitafetcher.data

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AppInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        try {
            val response = chain.proceed(original)
            if (response.isSuccessful) {
                val bodyString = response.body?.string() ?: throw AppException("data not found")
                val newResponseBody = bodyString.toResponseBody("application/json".toMediaType())
                return response.newBuilder().body(newResponseBody).build()
            } else {
                throw AppException("response error")
            }
        } catch (e: AppException) {
            throw e
        }
    }
}
