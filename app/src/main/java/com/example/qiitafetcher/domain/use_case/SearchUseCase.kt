package com.example.qiitafetcher.domain.use_case

import android.content.Context
import android.content.SharedPreferences
import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.data.repository.SaveArticlesRepository
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: ArticlesRepository,
    private val saveArticlesRepository: SaveArticlesRepository,
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * 検索履歴の読み込み
     */
    internal fun loadSearchHistory(): List<String> {
        return sharedPreferences.getStringSet(SEARCH_HISTORY_LIST_KEY, emptySet())?.toList()
            ?: emptyList()
    }

    /**
     * 検索履歴の保存
     */
    internal fun saveSearchHistory(keyword: String) {
        val currentHistory = loadSearchHistory().toMutableList()
        currentHistory.remove(keyword)
        currentHistory.add(0, keyword)
        val editor = sharedPreferences.edit()
        editor.putStringSet(SEARCH_HISTORY_LIST_KEY, currentHistory.toSet())
        editor.apply()
    }

    /**
     * 検索履歴の削除
     */
    internal fun deleteSearchHistory(keyword: String) {
        val currentHistory = loadSearchHistory().toMutableList()
        currentHistory.remove(keyword)
        val editor = sharedPreferences.edit()
        editor.putStringSet(SEARCH_HISTORY_LIST_KEY, currentHistory.toSet())
        editor.apply()
    }

    /**
     * 特定のキーワードで記事一覧を取得
     */
    internal suspend fun getArticleList(page: Int, query: String): List<ArticleItemUiModel> {
        // 正規表現にて全角と半角を許可する
        val machiningQuery = if (query.split(Regex("""[ 　]""")).size > 1) {
            // 複数キーワードの場合
            val keywords = query.split(Regex("""[ 　]""")).joinToString(",")
            "body:$keywords"
        } else {
            // 単一キーワードの場合
            "body:$query"
        }

        return repository.getArticleList(page = page, query = machiningQuery)
    }

    /**
     * 記事が保存されているか確認
     */
    internal suspend fun isArticleSaved(articleId: String): Boolean {
        return saveArticlesRepository.isArticleSaved(articleId)
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "search_history"
        private const val SEARCH_HISTORY_LIST_KEY = "search_history_list"
    }
}