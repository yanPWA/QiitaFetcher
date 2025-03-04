package com.example.qiitafetcher.network

import java.io.IOException

/**
 * エラーをラップするクラス
 */
data class AppException(
    override val message: String?,
    override val cause: Throwable? = null,
    val errorCode: Int? = null
) : IOException()