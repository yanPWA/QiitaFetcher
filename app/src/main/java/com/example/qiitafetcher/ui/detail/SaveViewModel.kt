package com.example.qiitafetcher.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<SaveListUiState> = MutableStateFlow(SaveListUiState.Init)
    internal val uiState: StateFlow<SaveListUiState> = _uiState

    private val _uiEvent: MutableStateFlow<List<SaveListUiEvent>> = MutableStateFlow(emptyList())
    internal val uiEvent: Flow<SaveListUiEvent?> get() = _uiEvent.map { it.firstOrNull() }

    /**
     * uiStateの更新
     */
    private fun notifyUiState(state: SaveListUiState) {
        _uiState.value = state
    }

    /**
     * uiEventの更新
     */
    private fun notifyUiEvent(event: SaveListUiEvent) {
        _uiEvent.value += event
    }

    /**
     * uiEventの消費
     */
    internal fun processedUiEvent(event: SaveListUiEvent) {
        _uiEvent.value = _uiEvent.value.filterNot { it == event }
    }

    /**
     * 保存記事一覧取得
     */
    internal fun getSaveList() = viewModelScope.launch {
        runCatching {
            notifyUiState(state = SaveListUiState.Fetching)
            // todo roomからデータ取得　repository or useCase
        }.onSuccess {
//            notifyUiState(state = SaveListUiState.Fetched(saveList = it))
        }.onFailure {
            notifyUiState(state = SaveListUiState.Error)
            notifyUiEvent(
                event = SaveListUiEvent.Error(
                    message = it.message ?: "エラーが発生しました"
                )
            )
        }
    }

    /**
     * 保存記事削除
     */
    internal fun deleteSave(id: String) = viewModelScope.launch {

    }
}

/**
 * 保存記事一覧画面の状態
 */
sealed interface SaveListUiState {
    /** 初期状態 */
    data object Init : SaveListUiState

    /** 取得中 */
    data object Fetching : SaveListUiState, Loading

    /** 読み込み完了 */
    data class Fetched(val saveList: List<ArticleItemUiModel>) : SaveListUiState

    /** 読み込みエラー */
    data object Error : SaveListUiState
}

/** 取得中 */
interface Loading

/**
 * Uiイベント
 */
sealed interface SaveListUiEvent {
    data class Error(val message: String = "エラーが発生しました") : SaveListUiEvent
}
