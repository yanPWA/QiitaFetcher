package com.example.qiitafetcher.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qiitafetcher.ui.search.SearchListRoute

/**
 * 共通のツールバー
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QFTopAppBar(
    navController: NavController,
    currentRoute: String,
    keyword: String
) {
    CenterAlignedTopAppBar(
        title = {
            when (currentRoute) {
                BottomNavItems.Home.route -> Text(text = "QiitaFetcher")
                BottomNavItems.Save.route -> Text(text = BottomNavItems.Save.title)
                BottomNavItems.Search.route -> Text(text = BottomNavItems.Search.title)
                SearchListRoute().createRoute() -> {
                    Text(
                        text = keyword,
                        modifier = Modifier.padding(start = 16.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                else -> Text(text = "")
            }
        },
        navigationIcon = {
            if (currentRoute == SearchListRoute().createRoute()) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
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
