package com.example.qiitafetcher.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.qiitafetcher.R
import com.example.qiitafetcher.ui.ArticleItem
import com.example.qiitafetcher.ui.ErrorDialog
import com.example.qiitafetcher.ui.LoadingScreen
import com.example.qiitafetcher.ui.NoArticle
import com.example.qiitafetcher.ui.Tag
import com.example.qiitafetcher.ui.Title
import com.example.qiitafetcher.ui.dpToSp
import com.example.qiitafetcher.ui.home.ArticlesUiEvent
import com.example.qiitafetcher.ui.home.Loading
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * 検索タブ
 */
@Composable
internal fun SearchRout(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)
    val searchHistoryList by viewModel.searchHistoryList.collectAsStateWithLifecycle()

    when (state) {
        SearchUiState.Init -> {
            SearchScreen(
                searchHistoryList = searchHistoryList,
                onSearch = viewModel::getArticleListByKeyword,
                onDelete = viewModel::deleteSearchHistory,
                modifier = modifier
            )
        }

        is SearchUiState.Fetched -> {
            // todo 検索TOPへ戻れるようにする
            SearchList(
                keyword = (state as SearchUiState.Fetched).keyword,
                isAppending = (state as SearchUiState.Fetched).isAppending,
                articles = (state as SearchUiState.Fetched).articles,
                navController = navController,
                onLoadMore = viewModel::getMoreArticleListByKeyword,
            )
        }

        is SearchUiState.InitialLoadError -> {
            ErrorScreen(
                keyword = (state as SearchUiState.InitialLoadError).keyword,
                onRefresh = viewModel::getArticleListByKeyword
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
            message = (uiEvent as ArticlesUiEvent.Error).message,
            onDismiss = { viewModel.processedUiEvent(event = uiEvent as SearchUiEvent.Error) }
        )
    }
}

@Composable
private fun SearchScreen(
    searchHistoryList: List<String>,
    onSearch: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = modifier.height(20.dp))
        SearchField(onSearch = onSearch)
        Spacer(modifier = modifier.height(20.dp))
        SearchHistory(
            searchHistoryList = searchHistoryList,
            onSearch = onSearch,
            onDelete = onDelete
        )
    }
}

/** 検索窓 */
@Composable
private fun SearchField(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val focusManager: FocusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("検索キーワードを入力") },
            maxLines = 1, // 1行のみに制限
            singleLine = true, // 1行のみに制限
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search // 確定ボタンを検索ボタンに変更
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(inputText)
                    focusManager.clearFocus()
                    inputText = ""
                }
            ),
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    IconButton(onClick = { inputText = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = null)
                    }
                }
            }
        )
        IconButton(
            onClick = {
                onSearch.invoke(inputText)
                focusManager.clearFocus()
                inputText = ""
            },
            modifier = modifier.padding(start = 5.dp)
        ) {
            Icon(Icons.Filled.Search, contentDescription = null)
        }
    }
}

/** 検索履歴 */
@Composable
private fun SearchHistory(
    searchHistoryList: List<String>,
    onSearch: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "◽️検索履歴",
            style = Title.copy(fontSize = dpToSp(20.dp)),
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
        )
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(searchHistoryList.size) { index ->
                SearchHistoryItem(
                    keyword = searchHistoryList[index],
                    onSearch = onSearch,
                    onDelete = onDelete
                )

                // 最後のアイテム以外に区切り線を表示
                if (index < searchHistoryList.size - 1) {
                    HorizontalDivider(thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun SearchHistoryItem(
    keyword: String,
    onSearch: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSearch.invoke(keyword) } // クリックで再検索
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyword,
            style = Tag,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f) // テキストが可能な限り横幅を広げる
                .padding(horizontal = 5.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onDelete.invoke(keyword) }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                modifier = modifier
            )
        }
    }
}

/** 検索結果一覧 */
@Composable
internal fun SearchList(
    navController: NavController,
    keyword: String,
    isAppending: Boolean,
    articles: List<ArticleItemUiModel>,
    onLoadMore: (String) -> Unit,
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
            .collect { onLoadMore(keyword) }
    }

    // 記事がない場合
    if (articles.isEmpty()) {
        NoArticle()
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item { Spacer(modifier = modifier.height(20.dp)) }

        items(articles.size) { index ->
            ArticleItem(article = articles[index], navController = navController)
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
private fun ErrorScreen(
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
private fun SearchFieldPreview() {
    SearchField(onSearch = {})
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        searchHistoryList = listOf("Android", "Kotlin", "Git", "Flow"),
        onSearch = {},
        onDelete = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchHistoryItemPreview() {
    SearchHistoryItem(
        keyword = "Android",
        onSearch = {},
        onDelete = {}
    )
}