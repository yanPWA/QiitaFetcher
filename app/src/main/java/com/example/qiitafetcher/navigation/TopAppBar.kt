package com.example.qiitafetcher.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.example.qiitafetcher.ui.search.SearchListRoute

/**
 * 共通のツールバー
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QFTopAppBar(currentRoute: String, keyword: String) {
    Log.d("QFTopAppBar","keyword -> $keyword")
    CenterAlignedTopAppBar(
        title = {
            when (currentRoute) {
                BottomNavItems.Home.route -> Text(text = "QiitaFetcher")
                BottomNavItems.Save.route -> Text(text = BottomNavItems.Save.title)
                BottomNavItems.Search.route -> Text(text = BottomNavItems.Search.title)
                SearchListRoute().createRoute() -> {
                    Text(
                        text = keyword,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                else -> Text(text = "")
            }
        },
        actions = {
            IconButton(onClick = { /* 設定画面への遷移処理 */ }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            }
        }
    )
}
