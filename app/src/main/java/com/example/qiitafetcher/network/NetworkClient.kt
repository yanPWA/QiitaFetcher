package com.example.qiitafetcher.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlinx.serialization.json.Json

private const val BASE_URL ="https://qiita.com/api/v2/"

class NetworkClient {

    private val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    /**
     * okhttpクライアントを生成する
     */
    private val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(AppInterceptor())
        .build()

    /**
     * retrofitクライアントを生成する
     */
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

}