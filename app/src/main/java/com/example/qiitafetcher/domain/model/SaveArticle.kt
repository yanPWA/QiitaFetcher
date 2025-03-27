package com.example.qiitafetcher.domain.model

import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel

/**
 * 記事を保存するEntity
 */
@Entity(tableName = "save_articles")
data class SaveArticle(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: String?,
    @ColumnInfo(name = "likes_count") val likesCount: Int?,
    @ColumnInfo(name = "tags") val tags: List<Tags>?,
    @ColumnInfo(name = "is_saved") val isSaved: Boolean
)

/**
 * List<SaveArticle> -> List<ArticleItemUiModel>へ変換
 */
internal fun List<SaveArticle>.convertToArticleItemUiModel(): List<ArticleItemUiModel> {
    return this.map { saveArticle ->
        ArticleItemUiModel(
            imageUrl = saveArticle.imageUrl,
            userName = saveArticle.userName ?: "",
            updatedAt = saveArticle.updatedAt ?: "",
            title = saveArticle.title ?: "",
            tags = saveArticle.tags,
            likesCount = saveArticle.likesCount ?: 0,
            /** 詳細表示用のURL */
            url = saveArticle.url ?: "",
            isSaved = mutableStateOf(true)
        )
    }
}

/**
 * ArticleItemUiModel -> SaveArticleへ変換
 */
internal fun ArticleItemUiModel.convertToSaveArticle(): SaveArticle {
    return SaveArticle(
        title = this.title,
        url = this.url,
        userName = this.userName,
        imageUrl = this.imageUrl,
        updatedAt = this.updatedAt,
        likesCount = this.likesCount,
        tags = this.tags,
        id = this.url,
        isSaved = this.isSaved.value
    )
}
