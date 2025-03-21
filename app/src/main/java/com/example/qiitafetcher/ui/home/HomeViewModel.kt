package com.example.qiitafetcher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ARTICLES_PAGE_INITIAL = 1

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ArticlesRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<ArticlesUiState> = MutableStateFlow(ArticlesUiState.Init)
    internal val uiState: StateFlow<ArticlesUiState> = _uiState

    private val _uiEvent: MutableStateFlow<List<ArticlesUiEvent>> = MutableStateFlow(emptyList())
    internal val uiEvent: Flow<ArticlesUiEvent?> get() = _uiEvent.map { it.firstOrNull() }

    private fun currentState(): ArticlesUiState = uiState.value

    // 初回取得フラグ
    private var isFirstLoad = true

    // 読み込み重複制御用
    private var isLoading = false

    /**
     * uiStateの更新
     */
    private fun notifyUiState(state: ArticlesUiState) {
        _uiState.value = state
    }

    /**
     * uiEventの更新
     */
    private fun notifyUiEvent(event: ArticlesUiEvent) {
        _uiEvent.value += event
    }

    /**
     * uiEventの消費
     */
    internal fun processedUiEvent(event: ArticlesUiEvent) {
        _uiEvent.value = _uiEvent.value.filterNot { it == event }
    }

    /**
     * 記事一覧取得
     */
    internal fun getArticleList() = viewModelScope.launch {
        // 初回のみ取得
        if (!isFirstLoad) return@launch

        // 重複制御
        if (isLoading) return@launch
        isLoading = true

        notifyUiState(state = ArticlesUiState.Fetching)
        runCatching {
            repository.getArticleList(page = ARTICLES_PAGE_INITIAL)
        }.onSuccess {
            notifyUiState(state = ArticlesUiState.Fetched(articles = it))
        }.onFailure {
            notifyUiState(ArticlesUiState.InitialLoadError())
            notifyUiEvent(ArticlesUiEvent.Error(message = it.message ?: "エラーが発生しました"))
        }.also {
            isFirstLoad = false
            isLoading = false
        }
    }

    /**
     * 記事一覧追加読み込み
     */
    internal fun getMoreArticleList() = viewModelScope.launch {
        // 重複制御
        if (isLoading) return@launch
        isLoading = true

        val currentState = currentState() as? ArticlesUiState.Fetched ?: return@launch
        val nextPage = currentState.page + 1
        notifyUiState(
            state = ArticlesUiState.Fetched(
                page = currentState.page,
                articles = currentState.articles,
                isAppending = true
            )
        )
        runCatching {
            repository.getArticleList(page = nextPage)
        }.onSuccess {
            if (it.isEmpty()) {
                notifyUiState(
                    state = ArticlesUiState.Fetched(
                        page = nextPage,
                        articles = currentState.articles,
                        isAppending = false
                    )
                )
            } else {
                notifyUiState(
                    state = ArticlesUiState.Fetched(
                        page = nextPage,
                        articles = currentState.articles + it,
                        isAppending = false
                    )
                )
            }
        }.onFailure {
            notifyUiState(
                state = ArticlesUiState.Fetched(
                    page = nextPage,
                    articles = currentState.articles,
                    // くるくるを止める
                    isAppending = false
                )
            )
            notifyUiEvent(ArticlesUiEvent.Error(message = it.message ?: "エラーが発生しました"))
        }.also {
            isLoading = false
        }
    }
}

/**
 * 記事一覧画面の状態
 */
sealed interface ArticlesUiState {
    /** 初期状態 */
    data object Init : ArticlesUiState

    /** 取得中 */
    data object Fetching : ArticlesUiState, Loading

    /** 読み込み完了 */
    data class Fetched(
        val page: Int = ARTICLES_PAGE_INITIAL,
        val articles: List<ArticleItemUiModel>,
        /** 追加取得かどうか */
        val isAppending: Boolean = false
    ) : ArticlesUiState

    /** 初回読み込みエラー */
    data class InitialLoadError(val page: Int = ARTICLES_PAGE_INITIAL) : ArticlesUiState
}

/** 取得中 */
interface Loading

/**
 * Uiイベント
 */
sealed interface ArticlesUiEvent {
    data class Error(val message: String = "エラーが発生しました") : ArticlesUiEvent
}
