package com.example.qiitafetcher.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.qiitafetcher.ui.detail.detailScreen
import com.example.qiitafetcher.ui.home.HomeRout
import com.example.qiitafetcher.ui.save.SaveRout
import com.example.qiitafetcher.ui.search.SearchRout
import com.example.qiitafetcher.ui.search.SearchViewModel
import com.example.qiitafetcher.ui.search.searchList

@Composable
fun QFNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val searchViewModel: SearchViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = BottomNavItems.Home.route,
        modifier = modifier
    ) {
        composable(route = BottomNavItems.Home.route) {
            HomeRout(navController)
        }

        composable(route = BottomNavItems.Save.route) {
            SaveRout(navController)
        }

        composable(route = BottomNavItems.Search.route) {
            SearchRout(
                navController = navController,
                viewModel = searchViewModel
            )
        }

        searchList(
            navController = navController,
            viewModel = searchViewModel
        )

        detailScreen(
            showBackButton = false,
            onBackClick = navController::popBackStack
        )
    }
}
