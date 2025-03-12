package com.example.qiitafetcher.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qiitafetcher.ui.navigation.BottomNavigation
import com.example.qiitafetcher.ui.navigation.Route
import com.example.qiitafetcher.ui.theme.QiitaFetcherTheme
import com.example.qiitafetcher.ui.ui_model.DetailScreen

@Composable
fun QiitaRout(viewModel: ArticlesViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    QiitaFetcherTheme {
        Scaffold(
            bottomBar = { BottomNavigation(navController) }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Route.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Route.Home.route) {
                    HomeRout(navController, viewModel)
                }
//                composable(route = Route.Search.route) {
//                    SearchScreen(navController)
//                }
//                composable(route = Route.Favorite.route) {
//                    FavoriteScreen(navController)
//                }
                composable(
                    route = Route.Detail.route,
                    arguments = listOf(navArgument("url") { type = NavType.StringType })
                ) { backStackEntry ->
                    val url = backStackEntry.arguments?.getString("url") ?: ""
                    DetailScreen(navController = navController, url = url)
                }
            }
        }
    }
}
