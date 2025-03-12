package com.example.qiitafetcher.ui

import android.content.Context
import android.widget.Toast

object UiUtils {
    /**
     * Toastを表示する
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}