package com.example.qiitafetcher.data.repository

import com.example.qiitafetcher.data.api.QiitaService
import com.example.qiitafetcher.domain.model.convertToArticleItemUiModel
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val ARTICLES_PER_PAGE = 20

/**
 * 記事一覧を取得
 */
interface ArticlesRepository {
    suspend fun getArticleList(
        page: Int,
        perPage: Int = ARTICLES_PER_PAGE,
        query: String? = null
    ): List<ArticleItemUiModel>
}

class ArticlesRepositoryImpl @Inject constructor(
    private val api: QiitaService.ItemsApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ArticlesRepository {
    override suspend fun getArticleList(
        page: Int,
        perPage: Int,
        query: String?
    ): List<ArticleItemUiModel> =
        withContext(defaultDispatcher) {
            api.getItems(
                page = page,
                perPage = perPage,
                query = query
            ).map { it.convertToArticleItemUiModel() }
        }
}
