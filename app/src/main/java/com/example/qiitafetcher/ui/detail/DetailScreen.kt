package com.example.qiitafetcher.ui.detail

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.qiitafetcher.R
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val url: String = "") {
    companion object {
        const val ROUTE = "detail"
        const val URL_ARG = "url"
    }

    fun createRoute(): String {
        return "$ROUTE/{$URL_ARG}"
    }
}

fun NavController.navigateToDetail(detailRoute: DetailRoute) {
    navigate(route = "${DetailRoute.ROUTE}/${detailRoute.url}")
}

fun NavGraphBuilder.detailScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit
) {
    composable(route = DetailRoute().createRoute()) {
        DetailScreen(
            showBackButton = showBackButton,
            onBackClick = onBackClick,
        )
    }
}

/** 記事詳細 */
@Composable
internal fun DetailScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val detailUiState: DetailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    DetailScreen(
        detailUiState = detailUiState,
        notifyDetailUiState = viewModel::notifyDetailUiState,
        showBackButton = showBackButton,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
internal fun DetailScreen(
    detailUiState: DetailUiState,
    notifyDetailUiState: (DetailUiState) -> Unit,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (detailUiState) {
        is DetailUiState.WebView -> {
            DetailScreen(
                state = detailUiState,
                url = detailUiState.url,
                notifyDetailUiState = notifyDetailUiState,
                showBackButton = showBackButton,
                onBackClick = onBackClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DetailScreen(
    state: DetailUiState.WebView,
    url: String,
    notifyDetailUiState: (DetailUiState) -> Unit,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webView: WebView? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier.padding(top = 35.dp, bottom = 8.dp)
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            notifyDetailUiState.invoke(
                                DetailUiState.WebView(
                                    url = url ?: "",
                                    canBack = canGoBack(),
                                    canForward = canGoForward()
                                )
                            )
                        }
                    }
                    loadUrl(state.url)
                    webView = this
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .weight(1F)
        )

        CustomBar(
            showBackButton = showBackButton,
            webView = webView ?: return,
            onClose = onBackClick
        )
    }
}

/** カスタムバー */
@Composable
private fun CustomBar(
    showBackButton: Boolean,
    webView: WebView,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val customBarItems = createCustomBarItems(
        webView = webView,
        onClose = onClose
    )

    Row(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        customBarItems.forEach { item ->
            when (item) {
                is CustomBarItems.Back -> {
                    if (webView.canGoBack()) {
                        Icon(
                            painter = painterResource(item.iconResId),
                            contentDescription = null,
                            modifier = modifier.clickable { item.onClick.invoke() }
                        )
                    } else {
                        Spacer(modifier = modifier.iconSize())
                    }
                }

                is CustomBarItems.Forward -> {
                    if (webView.canGoForward()) {
                        Icon(
                            painter = painterResource(item.iconResId),
                            contentDescription = null,
                            modifier = modifier.clickable { item.onClick.invoke() }
                        )
                    } else {
                        Spacer(modifier = modifier.iconSize())
                    }
                }

                else -> {
                    Icon(
                        painter = painterResource(item.iconResId),
                        contentDescription = null,
                        modifier = modifier.clickable { item.onClick.invoke() }
                    )
                }
            }
        }
    }
}

private fun Modifier.iconSize(): Modifier {
    return this.then(
        Modifier
            .width(44.dp)
            .height(24.dp)
    )
}

sealed class CustomBarItems(open val iconResId: Int, open val onClick: () -> Unit) {
    data class Back(
        override val iconResId: Int = R.drawable.arrow_back,
        override val onClick: () -> Unit,
        val isEnable: Boolean
    ) : CustomBarItems(iconResId = iconResId, onClick = onClick)

    data class Forward(
        override val iconResId: Int = R.drawable.arrow_forward,
        override val onClick: () -> Unit,
        val isEnable: Boolean
    ) : CustomBarItems(iconResId = iconResId, onClick = onClick)

    data class Refresh(
        override val iconResId: Int = R.drawable.refresh,
        override val onClick: () -> Unit
    ) : CustomBarItems(iconResId = iconResId, onClick = onClick)

    data class Share(
        override val iconResId: Int = R.drawable.share,
        override val onClick: () -> Unit
    ) : CustomBarItems(iconResId = iconResId, onClick = onClick)

    data class Close(
        override val iconResId: Int = R.drawable.close,
        override val onClick: () -> Unit
    ) : CustomBarItems(iconResId = iconResId, onClick = onClick)
}

private fun createCustomBarItems(
    webView: WebView,
    onClose: () -> Unit
): List<CustomBarItems> {
    return listOf(
        CustomBarItems.Back(onClick = webView::goBack, isEnable = webView.canGoBack()),
        CustomBarItems.Forward(onClick = webView::goForward, isEnable = webView.canGoForward()),
        CustomBarItems.Refresh(onClick = webView::reload),
        CustomBarItems.Share(onClick = {/* todo メニュー */ }),
        CustomBarItems.Close(
            onClick = {
                webView.destroy()
                webView.clearHistory()
                webView.clearCache(true)
                onClose.invoke()
            })
    )
}
