package com.example.qiitafetcher.data

import java.io.IOException

/**
 * エラーをラップするクラス　todo exception周りはもっと改善したい
 */
data class AppException(
    override val message: String?,
    override val cause: Throwable? = null,
    val errorCode: Int? = null
) : IOException()