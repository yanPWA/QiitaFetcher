package com.example.qiitafetcher.ui.save

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.qiitafetcher.ui.ErrorScreen
import com.example.qiitafetcher.ui.detail.SaveListUiState
import com.example.qiitafetcher.ui.detail.SaveViewModel
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel

/**
 * 保存一覧
 */
@Composable
internal fun SaveListRout(
    navController: NavController,
    viewModel: SaveViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        viewModel.getSaveList()
    }

    when (state) {
        SaveListUiState.Error -> {
            ErrorScreen(onRefresh = viewModel::getSaveList)
        }

        is SaveListUiState.Fetched -> {
            SaveList(
                navController = navController,
                saveList = (state as SaveListUiState.Fetched).saveList
            )
        }

        else -> {/* 何もしない */
        }
    }
}

@Composable
private fun SaveList(
    navController: NavController,
    saveList: List<ArticleItemUiModel>,
    modifier: Modifier = Modifier
) {

}