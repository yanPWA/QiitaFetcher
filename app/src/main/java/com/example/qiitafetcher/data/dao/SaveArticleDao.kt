package com.example.qiitafetcher.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.qiitafetcher.domain.model.SaveArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveArticleDao {
    /** 全ての保存記事を取得 */
    @Query("SELECT * FROM save_articles")
    fun getAllSavedArticles(): Flow<List<SaveArticle>>

    /** 記事を保存 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(saveArticle: SaveArticle)

    /** 記事を削除 */
    @Delete
    suspend fun delete(saveArticle: SaveArticle)

    /** 記事が保存されているか確認 */
    @Query("SELECT EXISTS(SELECT 1 FROM save_articles WHERE id = :articleId)")
    suspend fun isArticleSaved(articleId: String): Boolean
}
