package com.example.qiitafetcher.data.model

import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 認証中のユーザーの記事
 */
@Serializable
data class Article(
    @SerialName("rendered_body")
    val renderedBody: String?,
    val body: String?,
    val coediting: Boolean?,
    @SerialName("comments_count")
    val commentsCount: Int?,
    @SerialName("created_at")
    val createdAt: String?,
    val group: Group?,
    val id: String?,
    @SerialName("likes_count")
    val likesCount: Int?,
    val private: Boolean?,
    @SerialName("reactions_count")
    val reactionsCount: Int?,
    @SerialName("stocks_count")
    val stocksCount: Int?,
    val tags: List<Tags?>?,
    val title: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    val url: String?,
    val user: User?,
    @SerialName("page_views_count")
    val pageViewsCount: Int?,
    @SerialName("team_membership")
    val teamMembership: TeamMembership?,
    @SerialName("organization_url_name")
    val organizationUrlName: String?,
    val slide: Boolean?
)

@Serializable
data class Group(
    @SerialName("created_at")
    val createdAt: String?,
    val description: String?,
    val name: String?,
    val private: Boolean?,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("url_name")
    val urlName: String?
)

@Serializable
data class Tags(
    val name: String?,
    val versions: List<String>?
)

@Serializable
data class User(
    val description: String?,
    @SerialName("facebook_id")
    val facebookId: String?,
    @SerialName("followees_count")
    val followeesCount: Int?,
    @SerialName("followers_count")
    val followersCount: Int?,
    @SerialName("github_login_name")
    val githubLoginName: String?,
    val id: String?,
    @SerialName("items_count")
    val itemsCount: Int?,
    @SerialName("linkedin_id")
    val linkedinId: String?,
    val location: String?,
    val name: String?,
    val organization: String?,
    @SerialName("permanent_id")
    val permanentId: Int?,
    @SerialName("profile_image_url")
    val profileImageUrl: String?,
    @SerialName("team_only")
    val teamOnly: Boolean?,
    @SerialName("twitter_screen_name")
    val twitterScreenName: String?,
    @SerialName("website_url")
    val websiteUrl: String?
)

@Serializable
data class TeamMembership(
    val name: String?
)

/**
 * uiModel変換
 */
internal fun Article.convertToArticleItemUiModel(): ArticleItemUiModel {
    return ArticleItemUiModel(
        imageUrl = user?.profileImageUrl ?: "",
        userName = user?.id ?: "",
        updatedAt = updatedAt ?: "",
        title = title ?: "",
        tags = tags,
        likesCount = likesCount ?: 0
    )
}
