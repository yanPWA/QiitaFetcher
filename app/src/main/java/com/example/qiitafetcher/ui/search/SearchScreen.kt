package com.example.qiitafetcher.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.qiitafetcher.navigation.BottomNavItems
import com.example.qiitafetcher.ui.theme.QFTypography

/**
 * 検索タブ
 */
@Composable
internal fun SearchRout(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val searchHistoryList by viewModel.searchHistoryList.collectAsStateWithLifecycle()

    // 現在のバックスタックエントリを取得
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // バックスタックエントリが SearchRout に対応するものに変化した際に、viewModel.resetState() を呼び出す
    LaunchedEffect(navBackStackEntry) {
        if (navBackStackEntry?.destination?.route == BottomNavItems.Search.route) {
            viewModel.resetState()
        }
    }

    when (state) {
        SearchUiState.Init -> {
            SearchScreen(
                navController = navController,
                searchHistoryList = searchHistoryList,
                onSearch = viewModel::getArticleListByKeyword,
                onDelete = viewModel::deleteSearchHistory,
                modifier = modifier
            )
        }

        else -> {/* 何もしない */
        }
    }
}

@Composable
private fun SearchScreen(
    navController: NavController,
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
        SearchField(
            navController = navController,
            onSearch = onSearch
        )
        Spacer(modifier = modifier.height(20.dp))
        SearchHistory(
            navController = navController,
            searchHistoryList = searchHistoryList,
            onHistorySearch = onSearch,
            onDelete = onDelete
        )
    }
}

/** 検索窓 */
@Composable
private fun SearchField(
    navController: NavController,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val focusManager: FocusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
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
                    navController.navigateToSearchList(searchListRoute = SearchListRoute(keyword = inputText))
                    onSearch.invoke(inputText)
                    focusManager.clearFocus()
                    inputText = ""
                }
            ),
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    IconButton(onClick = {
                        navController.navigateToSearchList(searchListRoute = SearchListRoute(keyword = inputText))
                        onSearch.invoke(inputText)
                        focusManager.clearFocus()
                        inputText = ""
                    }) {
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
                navController.navigateToSearchList(searchListRoute = SearchListRoute(keyword = inputText))
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
    navController: NavController,
    searchHistoryList: List<String>,
    onHistorySearch: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "◽️検索履歴",
            style = QFTypography.titleLarge,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
        )
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(searchHistoryList.size) { index ->
                SearchHistoryItem(
                    navController = navController,
                    keyword = searchHistoryList[index],
                    onHistorySearch = onHistorySearch,
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
    navController: NavController,
    keyword: String,
    onHistorySearch: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController.navigateToSearchList(searchListRoute = SearchListRoute(keyword = keyword))
                onHistorySearch.invoke(keyword)
            }
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = keyword,
            style = QFTypography.labelLarge,
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

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    SearchField(
        navController = NavController(LocalContext.current),
        onSearch = {})
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        navController = NavController(LocalContext.current),
        searchHistoryList = listOf("Android", "Kotlin", "Git", "Flow"),
        onSearch = {},
        onDelete = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchHistoryItemPreview() {
    SearchHistoryItem(
        navController = NavController(LocalContext.current),
        keyword = "Android",
        onHistorySearch = {},
        onDelete = {}
    )
}