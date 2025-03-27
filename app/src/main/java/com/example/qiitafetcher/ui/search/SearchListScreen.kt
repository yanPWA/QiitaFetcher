package com.example.qiitafetcher.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.qiitafetcher.R
import com.example.qiitafetcher.ui.ArticleItem
import com.example.qiitafetcher.ui.ErrorDialog
import com.example.qiitafetcher.ui.LoadingScreen
import com.example.qiitafetcher.ui.NoArticle
import com.example.qiitafetcher.ui.createArticleItem
import com.example.qiitafetcher.ui.save.SaveArticlesViewModel
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.Serializable

@Serializable
data class SearchListRoute(val keyword: String = "") {
    companion object {
        const val ROUTE = "searchList"
        const val KEYWORD_ARG = "keyword"
    }

    fun createRoute(): String {
        return "$ROUTE/{$KEYWORD_ARG}"
    }
}

fun NavController.navigateToSearchList(searchListRoute: SearchListRoute) {
    navigate(route = "${SearchListRoute.ROUTE}/${searchListRoute.keyword}")
}

fun NavGraphBuilder.searchList(
    navController: NavController,
    viewModel: SearchViewModel
) {
    composable(
        route = SearchListRoute().createRoute(),
        arguments = listOf(navArgument(SearchListRoute.KEYWORD_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val keyword = backStackEntry.arguments?.getString(SearchListRoute.KEYWORD_ARG) ?: ""

        SearchListRout(
            navController = navController,
            backStackEntry = backStackEntry,
            searchViewModel = viewModel,
            keyword = keyword
        )
    }
}

/**
 * 検索結果一覧
 */
@Composable
internal fun SearchListRout(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    searchViewModel: SearchViewModel,
    saveArticlesViewModel: SaveArticlesViewModel = hiltViewModel(),
    keyword: String
) {
    val state by searchViewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by searchViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(keyword) {
        searchViewModel.getArticleListByKeyword(keyword)
    }

    when (state) {
        is SearchUiState.Fetched -> {
            SearchList(
                navController = navController,
                keyword = keyword,
                isAppending = (state as SearchUiState.Fetched).isAppending,
                articles = (state as SearchUiState.Fetched).articles,
                onSearch = searchViewModel::getArticleListByKeyword,
                onLoadMore = searchViewModel::getMoreArticleListByKeyword,
                resetState = searchViewModel::resetState,
                onSave = saveArticlesViewModel::saveArticle,
                onDelete = saveArticlesViewModel::deleteArticle
            )
        }

        else -> {/* 何もしない */
        }
    }

    /** 更新中表示 */
    if (state is Loading) {
        LoadingScreen(modifier = Modifier.fillMaxSize())
    }

    /** エラーダイアログ表示 */
    if (uiEvent is SearchUiEvent.Error) {
        ErrorDialog(
            message = (uiEvent as SearchUiEvent.Error).message,
            onDismiss = {
                navController.popBackStack()
                searchViewModel.processedUiEvent(event = uiEvent as SearchUiEvent.Error)
            }
        )
    }
}

/** 検索結果一覧 */
@Composable
internal fun SearchList(
    navController: NavController,
    keyword: String,
    isAppending: Boolean,
    articles: List<ArticleItemUiModel>,
    onSearch: (String) -> Unit,
    onLoadMore: (String) -> Unit,
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
            .collect { onLoadMore.invoke(keyword) }
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

/** エラー画面 */
@Composable
internal fun ErrorScreen(
    keyword: String,
    onRefresh: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // todo いずれボタンカスタム
        TextButton(onClick = { onRefresh.invoke(keyword) }) {
            Text(text = stringResource(R.string.retry_message))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchListPreview() {
    SearchList(
        navController = NavController(LocalContext.current),
        keyword = "Android",
        isAppending = false,
        articles = List(10) { createArticleItem() },
        onSearch = {},
        onLoadMore = {},
        resetState = {},
        onSave = {},
        onDelete = {}
    )
}
