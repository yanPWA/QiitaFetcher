package com.example.qiitafetcher.navigation

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
