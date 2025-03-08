package com.example.qiitafetcher.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.qiitafetcher.data.model.Article
import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel

@Composable
fun QiitaRout(articleList: List<ArticleItemUiModel>?, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // パディングを追加
    ) {

        articleList?.forEach { article ->
            item {
                ArticleItem(article = article)
            }
        } ?: item { Text(text = "記事がありません") }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun QiitaRoutPreview() {
//    QiitaFetcherTheme {
//        QiitaRout("Android")
//    }
//}
