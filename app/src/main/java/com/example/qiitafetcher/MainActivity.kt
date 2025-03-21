package com.example.qiitafetcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.qiitafetcher.navigation.BottomNavItems
import com.example.qiitafetcher.navigation.BottomNavigation
import com.example.qiitafetcher.navigation.QFNavHost
import com.example.qiitafetcher.navigation.QFTopAppBar
import com.example.qiitafetcher.ui.search.SearchListRoute
import com.example.qiitafetcher.ui.theme.QiitaFetcherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            QiitaFetcherTheme {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val keyword = if (currentRoute == SearchListRoute().createRoute()) {
                    navBackStackEntry?.arguments?.getString(SearchListRoute.KEYWORD_ARG) ?: ""
                } else {
                    ""
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute != null) {
                            // todo topBar非表示に遷移する際、遷移前にtopBarが消えるためUIががたつく
                            if (!currentRoute.startsWith("detail/")) {
                                QFTopAppBar(currentRoute = currentRoute, keyword = keyword)
                            }
                        }
                    },
                    bottomBar = {
                        val bottomNavRoutes = listOf(
                            BottomNavItems.Home.route,
                            BottomNavItems.Save.route,
                            BottomNavItems.Search.route,
                        )
                        if (currentRoute in bottomNavRoutes) {
                            BottomNavigation(navController)
                        }
                    },
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { padding ->
                    QFNavHost(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .consumeWindowInsets(padding)
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    )
                }
            }
        }
    }
}
