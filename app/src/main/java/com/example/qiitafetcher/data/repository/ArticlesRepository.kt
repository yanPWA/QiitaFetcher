package com.example.qiitafetcher.data.repository

import com.example.qiitafetcher.data.model.convertToArticleItemUiModel
import com.example.qiitafetcher.network.QiitaService
import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 記事一覧を取得
 */
interface ArticlesRepository {
    suspend fun getArticleList(): List<ArticleItemUiModel>
}

class ArticlesRepositoryImpl @Inject constructor(
    private val api: QiitaService.ItemsApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ArticlesRepository {
    override suspend fun getArticleList(): List<ArticleItemUiModel> =
        withContext(defaultDispatcher) {
            api.getItems(page = 1, perPage = 20).map { it.convertToArticleItemUiModel() }
        }
}
