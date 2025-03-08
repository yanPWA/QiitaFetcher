package com.example.qiitafetcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val repository: ArticlesRepository): ViewModel() {

    // todo flowへ
    var articleList = listOf<ArticleItemUiModel>()

    suspend fun getArticleList() = viewModelScope.launch {
        runCatching {
            // todo loading
            repository.getArticleList()
        }.onSuccess {
            articleList = it
        }.onFailure {
            // todo error
            it.printStackTrace()
        }
    }
}