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
        is ArticlesUiState.Error -> TODO()
        is ArticlesUiState.Fetched -> {
            ArticleList(articles = (state as ArticlesUiState.Fetched).articles)
        }

        else -> {/* 何もしない */
        }
    }

    /** 更新中表示 */
    if (state is Loading) {
//        todo LoadingScreen
    }
}

//@Preview(showBackground = true)
//@Composable
//fun QiitaRoutPreview() {
//    QiitaFetcherTheme {
//        QiitaRout("Android")
//    }
//}
