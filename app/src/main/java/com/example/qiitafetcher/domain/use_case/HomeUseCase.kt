package com.example.qiitafetcher.domain.use_case

import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.data.repository.SaveArticlesRepository
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository,
    private val saveArticlesRepository: SaveArticlesRepository
) {

    /**
     * 記事一覧を取得
     */
    internal suspend fun getArticleList(page: Int): List<ArticleItemUiModel> {
        return articlesRepository.getArticleList(page = page)
    }

    /**
     * 記事が保存されているか確認
     */
    internal suspend fun isArticleSaved(articleId: String):Boolean {
        return saveArticlesRepository.isArticleSaved(articleId)
    }
}
