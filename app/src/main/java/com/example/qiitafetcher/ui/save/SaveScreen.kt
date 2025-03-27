package com.example.qiitafetcher.ui.save

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.qiitafetcher.ui.ArticleItem
import com.example.qiitafetcher.ui.ErrorScreen
import com.example.qiitafetcher.ui.NoArticle
import com.example.qiitafetcher.ui.search.SearchViewModel
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel

/**
 * 保存一覧
 */
@Composable
internal fun SaveRout(
    navController: NavController,
    saveArticlesViewModel: SaveArticlesViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val state by saveArticlesViewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by saveArticlesViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        saveArticlesViewModel.getSaveList()
    }

    when (state) {
        SaveListUiState.Error -> {
            ErrorScreen(onRefresh = saveArticlesViewModel::getSaveList)
        }

        is SaveListUiState.Fetched -> {
            SaveList(
                navController = navController,
                saveList = (state as SaveListUiState.Fetched).saveList,
                onSearch = searchViewModel::getArticleListByKeyword,
                resetState = searchViewModel::resetState,
                onSave = saveArticlesViewModel::saveArticle,
                onDelete = saveArticlesViewModel::deleteArticle
            )
        }

        else -> {/* 何もしない */
        }
    }

    //todo uiEvent
}

@Composable
private fun SaveList(
    navController: NavController,
    saveList: List<ArticleItemUiModel>,
    onSearch: (String) -> Unit,
    resetState: () -> Unit,
    onSave: (ArticleItemUiModel) -> Unit,
    onDelete: (ArticleItemUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // 記事がない場合
    if (saveList.isEmpty()) {
        NoArticle()
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        items(saveList.size) { index ->
            ArticleItem(
                article = saveList[index],
                onSearch = onSearch,
                resetState = resetState,
                onSave = onSave,
                onDelete = onDelete,
                navController = navController
            )
        }
    }
}