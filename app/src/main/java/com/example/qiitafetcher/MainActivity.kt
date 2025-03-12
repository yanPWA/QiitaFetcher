package com.example.qiitafetcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qiitafetcher.data.repository.ArticlesRepositoryImpl
import com.example.qiitafetcher.ui.ArticlesViewModel
import com.example.qiitafetcher.ui.HomeRout
import com.example.qiitafetcher.ui.navigation.BottomNavItems
import com.example.qiitafetcher.ui.navigation.BottomNavigation
import com.example.qiitafetcher.ui.navigation.Routes
import com.example.qiitafetcher.ui.theme.QiitaFetcherTheme
import com.example.qiitafetcher.ui.ui_model.DetailScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ArticlesViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            QiitaFetcherTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigation(navController) }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItems.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = BottomNavItems.Home.route) {
                            HomeRout(navController, viewModel)
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

                        composable(
                            route = Routes.Detail.route,
                            arguments = listOf(navArgument("url") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("url") ?: ""
                            DetailScreen(navController = navController, url = url)
                        }
                    }
                }
            }
        }
    }
}
