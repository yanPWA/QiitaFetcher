package com.example.qiitafetcher.data.repository

import com.example.qiitafetcher.data.dao.SaveArticleDao
import com.example.qiitafetcher.domain.model.SaveArticle
import com.example.qiitafetcher.domain.model.convertToArticleItemUiModel
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 記事保存、削除をするリポジトリ
 */
interface SaveArticlesRepository {
    /** 全ての保存記事を取得 */
    suspend fun getAllSavedArticles(): List<ArticleItemUiModel>

    /** 記事を保存 */
    suspend fun insert(saveArticle: SaveArticle)

    /** 記事を削除 */
    suspend fun delete(saveArticle: SaveArticle)

    /** 記事が保存されているか確認 */
    suspend fun isArticleSaved(articleId: String): Boolean
}

class SaveArticlesRepositoryImpl @Inject constructor(private val saveArticleDao: SaveArticleDao) :
    SaveArticlesRepository {
    override suspend fun getAllSavedArticles(): List<ArticleItemUiModel> =
        saveArticleDao.getAllSavedArticles().map { saveArticles ->
            saveArticles.convertToArticleItemUiModel()
        }.first()

    override suspend fun insert(saveArticle: SaveArticle) {
        saveArticleDao.insert(saveArticle = saveArticle)
    }

    override suspend fun delete(saveArticle: SaveArticle) {
        saveArticleDao.delete(saveArticle = saveArticle)
    }

    override suspend fun isArticleSaved(articleId: String): Boolean {
        return saveArticleDao.isArticleSaved(articleId = articleId)
    }
}
