package com.example.qiitafetcher.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun QiitaRout(viewModel: ArticlesViewModel, modifier: Modifier = Modifier) {

    // todo 仮
    LaunchedEffect(Unit) {
        viewModel.getArticleList()
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is ArticlesUiState.Fetched -> {
            ArticleList(articles = (state as ArticlesUiState.Fetched).articles)
        }

        else -> {/* 何もしない */
        }
    }

    /** エラーダイアログ表示 */
    if (state is ArticlesUiState.Error) {
        ErrorDialog(message = (state as ArticlesUiState.Error).message)
        ErrorScreen(onRefresh = viewModel::getArticleList)
    }

    /** 更新中表示 */
    if (state is Loading) {
        LoadingScreen()
    }
}
