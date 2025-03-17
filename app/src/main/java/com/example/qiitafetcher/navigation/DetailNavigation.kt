package com.example.qiitafetcher.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.qiitafetcher.ui.detail.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val url: String = "")

fun NavController.navigateToDetail(url: String) {
    navigate(route = "detail/$url")
}

fun NavGraphBuilder.detailScreen(
    showBackButton: Boolean,
    onBackClick: () -> Unit
) {
    composable(route = "detail/{url}") {
        DetailScreen(
            showBackButton = showBackButton,
            onBackClick = onBackClick
        )
    }
}
