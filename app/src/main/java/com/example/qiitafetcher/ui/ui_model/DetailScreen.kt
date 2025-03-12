package com.example.qiitafetcher.ui.ui_model

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

/**
 * 記事詳細
 */
@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    url: String
) {
    var currentUrl by remember { mutableStateOf(url) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        currentUrl = url ?: ""
                    }
                }
                loadUrl(currentUrl)
            }
        },
        modifier = modifier
    )
}