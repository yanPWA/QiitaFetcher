package com.example.qiitafetcher.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AppInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // リクエストのログ出力
        Log.d("AppInterceptor", "Request URL: ${original.url}")
        Log.d("AppInterceptor", "Request Method: ${original.method}")
        Log.d("AppInterceptor", "Request Headers: ${original.headers}")
        original.body?.let {
            Log.d("AppInterceptor", "Request Body: ${it.toString()}")
        }

        try {
            val response = chain.proceed(original)

            // レスポンスのログ出力
            Log.d("AppInterceptor", "Response Code: ${response.code}")
            Log.d("AppInterceptor", "Response Message: ${response.message}")
            Log.d("AppInterceptor", "Response Headers: ${response.headers}")
            response.body?.let {
                val bodyString = it.string()
                Log.d("AppInterceptor", "Response Body: $bodyString")
                val newResponseBody = bodyString.toResponseBody("application/json".toMediaType())
                return response.newBuilder().body(newResponseBody).build()
            }

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
