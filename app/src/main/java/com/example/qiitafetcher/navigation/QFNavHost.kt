package com.example.qiitafetcher.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qiitafetcher.ui.home.HomeRout

@Composable
fun QFNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItems.Home.route,
        modifier = modifier
    ) {
        composable(route = BottomNavItems.Home.route) {
            HomeRout(navController)
        }

//                        composable(route = BottomNavItems.Save.route) {
//                            SaveScreen(navController)
//                        }
//
//                        composable(route = Route.Search.route) {
//                            SearchScreen(navController)
//                        }

//                        todo ダークモード 、 ライトモード対応 、 アプリバージョン
//                        composable(route = Route.Settings.route) {
//                            SettingsScreen(navController)
//                        }

        // ホームタブの一覧にて任意のアイテム押下で詳細遷移
        detailScreen(
            showBackButton = false,
            onBackClick = navController::popBackStack
        )
    }
}
