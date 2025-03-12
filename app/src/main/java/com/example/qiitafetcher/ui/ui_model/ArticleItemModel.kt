package com.example.qiitafetcher.ui.ui_model

import com.example.qiitafetcher.domain.model.Tags

/**
 * Qiita記事一覧で表示する情報
 */
data class ArticleItemUiModel(
    val imageUrl: String?,
    val userName: String,
    val updatedAt: String,
    val title: String,
    val tags: List<Tags>?,
    val likesCount: Int,
    val url: String
)