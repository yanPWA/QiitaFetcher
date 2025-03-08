package com.example.qiitafetcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val repository: ArticlesRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<ArticlesUiState> = MutableStateFlow(ArticlesUiState.Init)
    internal val uiState: StateFlow<ArticlesUiState> = _uiState

    /**
     * uiStateの更新
     */
    private fun notifyUiState(state: ArticlesUiState) {
        _uiState.value = state
    }

    internal fun getArticleList() = viewModelScope.launch {
        notifyUiState(ArticlesUiState.Fetching)
        runCatching {
            repository.getArticleList()
        }.onSuccess {
            notifyUiState(ArticlesUiState.Fetched(it))
        }.onFailure {
            notifyUiState(ArticlesUiState.Error(it.message ?: "error"))
            it.printStackTrace()
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
    data class Fetched(val articles: List<ArticleItemUiModel>) : ArticlesUiState

    /** エラー */
    data class Error(val message: String) : ArticlesUiState
}

/** 取得中 */
interface Loading

