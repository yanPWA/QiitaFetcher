package com.example.qiitafetcher.ui.navigation

sealed class Route(val route: String) {
    data object Home : Route("home")
    data object Favorite : Route("favorite")
    data object Search : Route("search")
    data object Detail : Route("detail/{url}") {
        fun createRoute(url: String) = "detail/$url"
    }
}