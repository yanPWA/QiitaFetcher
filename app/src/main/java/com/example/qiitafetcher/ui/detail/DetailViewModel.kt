package com.example.qiitafetcher.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _detailUiState: MutableStateFlow<DetailUiState> = MutableStateFlow(
        DetailUiState.WebView(
            url = savedStateHandle.toRoute<DetailRoute>().url,
            canBack = false,
            canForward = false
        )
    )

    internal val detailUiState: StateFlow<DetailUiState> = _detailUiState

    internal fun notifyDetailUiState(state: DetailUiState) {
        _detailUiState.value = state
    }
}

sealed interface DetailUiState {
    data class WebView(
        val url: String,
        val canBack: Boolean,
        val canForward: Boolean
    ) : DetailUiState
}
