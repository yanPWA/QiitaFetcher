package com.example.qiitafetcher.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.qiitafetcher.ui.ArticleItem
import com.example.qiitafetcher.ui.ErrorDialog
import com.example.qiitafetcher.ui.ErrorScreen
import com.example.qiitafetcher.ui.LoadingScreen
import com.example.qiitafetcher.ui.NoArticle
import com.example.qiitafetcher.ui.createTags
import com.example.qiitafetcher.ui.save.SaveArticlesViewModel
import com.example.qiitafetcher.ui.search.SearchViewModel
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * ホームタブ
 */
@Composable
internal fun HomeRout(
    navController: NavController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    saveArticlesViewModel: SaveArticlesViewModel = hiltViewModel(),
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by homeViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        homeViewModel.getArticleList()
    }

    when (state) {
        is ArticlesUiState.Fetched -> {
            ArticleList(
                isAppending = (state as ArticlesUiState.Fetched).isAppending,
                articles = (state as ArticlesUiState.Fetched).articles,
                navController = navController,
                onLoadMore = homeViewModel::getMoreArticleList,
                onSearch = searchViewModel::getArticleListByKeyword,
                resetState = searchViewModel::resetState,
                onSave = saveArticlesViewModel::saveArticle,
                onDelete = saveArticlesViewModel::deleteArticle
            )
        }

        is ArticlesUiState.InitialLoadError -> {
            ErrorScreen(onRefresh = homeViewModel::getArticleList)
        }

        else -> {/* 何もしない */
        }
    }

    /** 更新中表示 */
    if (state is Loading) {
        LoadingScreen(modifier = Modifier.fillMaxSize())
    }

    /** エラーダイアログ表示 */
    if (uiEvent is ArticlesUiEvent.Error) {
        ErrorDialog(
            message = (uiEvent as ArticlesUiEvent.Error).message,
            onDismiss = { homeViewModel.processedUiEvent(event = uiEvent as ArticlesUiEvent.Error) }
        )
    }
}

/** Qiita記事一覧 */
@Composable
private fun ArticleList(
    navController: NavController,
    isAppending: Boolean,
    articles: List<ArticleItemUiModel>,
    onLoadMore: () -> Unit,
    onSearch: (String) -> Unit,
    resetState: () -> Unit,
    onSave: (ArticleItemUiModel) -> Unit,
    onDelete: (ArticleItemUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // 一番下までスクロールされたかどうかを監視
    val isReachedBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index == layoutInfo.totalItemsCount - 1
            }
        }
    }

    // 一番下までスクロールされたら追加読み込み
    LaunchedEffect(isReachedBottom) {
        snapshotFlow { isReachedBottom }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }

    // 記事がない場合
    if (articles.isEmpty()) {
        NoArticle()
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        items(articles.size) { index ->
            ArticleItem(
                article = articles[index],
                onSearch = onSearch,
                resetState = resetState,
                onSave = onSave,
                onDelete = onDelete,
                navController = navController
            )
        }

        if (isAppending) {
            item {
                LoadingScreen(modifier = modifier.fillMaxWidth())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleListPreview() {
    ArticleList(
        navController = NavController(LocalContext.current),
        isAppending = false,
        articles = createArticles(),
        onLoadMore = {},
        onSearch = {},
        resetState = {},
        onSave = {},
        onDelete = {}
    )
}

internal fun createArticles(): List<ArticleItemUiModel> {
    return List(20) { index ->
        ArticleItemUiModel(
            imageUrl = null,
            userName = "yanP$index",
            updatedAt = "2023-09-01",
            title = "タイトル$index",
            tags = createTags(),
            likesCount = index,
            url = ""
        )
    }
}
