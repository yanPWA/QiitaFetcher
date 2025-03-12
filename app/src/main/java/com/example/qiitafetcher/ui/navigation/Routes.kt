package com.example.qiitafetcher.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.qiitafetcher.R

/**
 * BottomNavigation
 */
sealed class BottomNavItems(
    val route: String,
    val title: String,
    val iconResId: Int
) {
    data object Home : BottomNavItems("home", "Home", R.drawable.home)
    data object Save : BottomNavItems("save", "Save", R.drawable.bookmark)
    data object Search : BottomNavItems("search", "Search", R.drawable.search)
    data object Settings : BottomNavItems("settings", "Settings", R.drawable.settings)
}

val bottomNavItems = listOf(
    BottomNavItems.Home,
    BottomNavItems.Save,
    BottomNavItems.Search,
    BottomNavItems.Settings
)

/**
 * 画面遷移先
 */
sealed class Routes(val route: String) {
    data object Detail : Routes("detail/{url}") {
        fun createRoute(url: String) = "detail/$url"
    }
}
