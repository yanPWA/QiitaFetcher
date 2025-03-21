package com.example.qiitafetcher.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qiitafetcher.domain.use_case.SearchUseCase
import com.example.qiitafetcher.ui.home.ARTICLES_PAGE_INITIAL
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.Init)
    internal val uiState: StateFlow<SearchUiState> = _uiState

    private val _uiEvent: MutableStateFlow<List<SearchUiEvent>> = MutableStateFlow(emptyList())
    internal val uiEvent: Flow<SearchUiEvent?> get() = _uiEvent.map { it.firstOrNull() }

    // 検索履歴一覧
    private val _searchHistoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(searchUseCase.loadSearchHistory())
    internal val searchHistoryList: StateFlow<List<String>> get() = _searchHistoryList

    private fun currentState(): SearchUiState = uiState.value

    // 初回取得フラグ
    private var isFirstLoad = true

    // 読み込み重複制御用
    private var isLoading = false

    /**
     * uiStateの更新
     */
    private fun notifyUiState(state: SearchUiState) {
        _uiState.value = state
    }

    /**
     * uiEventの更新
     */
    private fun notifyUiEvent(event: SearchUiEvent) {
        _uiEvent.value += event
    }

    /**
     * uiEventの消費
     */
    internal fun processedUiEvent(event: SearchUiEvent) {
        _uiEvent.value = _uiEvent.value.filterNot { it == event }
    }

    /**
     * 特定のキーワードで記事一覧を取得 todo title:Git body:Ruby のような検索ワード
     */
    internal fun getArticleListByKeyword(keyword: String) = viewModelScope.launch {
        // 初回のみ取得
        if (!isFirstLoad) return@launch

        // 重複制御
        if (isLoading) return@launch
        isLoading = true

        notifyUiState(state = SearchUiState.Fetching)
        runCatching {
            searchUseCase.getArticleList(
                page = ARTICLES_PAGE_INITIAL,
                query = keyword
            )
        }.onSuccess {
            addSearchHistory(keyword)
            notifyUiState(state = SearchUiState.Fetched(articles = it, keyword = keyword))
        }.onFailure {
            notifyUiState(SearchUiState.InitialLoadError(keyword = keyword))
            notifyUiEvent(SearchUiEvent.Error(message = it.message ?: "エラーが発生しました"))
        }.also {
            isLoading = false
            isFirstLoad = false
        }
    }

    /**
     * 特定のキーワードで記事一覧を追加読み込み
     */
    internal fun getMoreArticleListByKeyword(keyword: String) = viewModelScope.launch {
        // 重複制御
        if (isLoading) return@launch
        isLoading = true

        val currentState = currentState() as? SearchUiState.Fetched ?: return@launch
        val nextPage = currentState.page + 1
        notifyUiState(
            state = SearchUiState.Fetched(
                page = currentState.page,
                articles = currentState.articles,
                isAppending = true,
                keyword = keyword
            )
        )
        runCatching {
            searchUseCase.getArticleList(
                page = nextPage,
                query = keyword
            )
        }.onSuccess {
            if (it.isEmpty()) {
                notifyUiState(
                    state = SearchUiState.Fetched(
                        page = nextPage,
                        articles = currentState.articles,
                        isAppending = false,
                        keyword = keyword
                    )
                )
            } else {
                notifyUiState(
                    state = SearchUiState.Fetched(
                        page = nextPage,
                        articles = currentState.articles + it,
                        isAppending = false,
                        keyword = keyword
                    )
                )
            }
        }.onFailure {
            notifyUiState(
                state = SearchUiState.Fetched(
                    page = nextPage,
                    articles = currentState.articles,
                    // くるくるを止める
                    isAppending = false,
                    keyword = keyword
                )
            )
            notifyUiEvent(SearchUiEvent.Error(message = it.message ?: "エラーが発生しました"))
        }.also {
            isLoading = false
        }
    }

    /**
     * 検索履歴に追加
     */
    private fun addSearchHistory(searchText: String) {
        // スペースで分割し、空文字を除外
        val keywords = searchText.split(Regex("[\\s　]+"))
            .filter { it.isNotBlank() }
            .distinct() // 重複を削除

        val currentList = _searchHistoryList.value.toMutableList()
        keywords.forEach { keyword ->
            // 重複を避けるために、すでに存在する場合は削除してから追加
            currentList.remove(keyword)
            currentList.add(0, keyword)
            searchUseCase.saveSearchHistory(keyword)
        }
        _searchHistoryList.value = currentList.toList()
    }

    /**
     * 検索履歴から削除 todo sharedPreferencesから削除できてない
     */
    internal fun deleteSearchHistory(searchText: String) {
        val currentList = _searchHistoryList.value.toMutableList()
        currentList.remove(searchText)
        _searchHistoryList.value = currentList.toList()
    }

    internal fun resetState() {
        notifyUiState(state = SearchUiState.Init)
        isFirstLoad = true
    }
}

/**
 * 検索画面の状態
 */
sealed interface SearchUiState {
    /** 初期状態 */
    data object Init : SearchUiState

    /** 取得中 */
    data object Fetching : SearchUiState, Loading

    /** 読み込み完了 */
    data class Fetched(
        val page: Int = ARTICLES_PAGE_INITIAL,
        val articles: List<ArticleItemUiModel>,
        /** 追加取得かどうか */
        val isAppending: Boolean = false,
        val keyword: String
    ) : SearchUiState

    /** 初回読み込みエラー */
    data class InitialLoadError(
        val keyword: String,
        val page: Int = ARTICLES_PAGE_INITIAL
    ) : SearchUiState
}

/** 取得中 */
interface Loading

/**
 * Uiイベント
 */
sealed interface SearchUiEvent {
    data class Error(val message: String = "エラーが発生しました") : SearchUiEvent
}
