package com.example.qiitafetcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.qiitafetcher.R
import com.example.qiitafetcher.domain.model.Tags
import com.example.qiitafetcher.ui.UiUtils.showToast
import com.example.qiitafetcher.ui.detail.DetailRoute
import com.example.qiitafetcher.ui.detail.navigateToDetail
import com.example.qiitafetcher.ui.search.SearchListRoute
import com.example.qiitafetcher.ui.search.navigateToSearchList
import com.example.qiitafetcher.ui.theme.QFTypography
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Qiita記事セル */
@Composable
internal fun ArticleItem(
    modifier: Modifier = Modifier,
    article: ArticleItemUiModel,
    onSearch: (String) -> Unit,
    resetState: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp, start = 5.dp, end = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                val encodedUrl =
                    URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                navController.navigateToDetail(detailRoute = DetailRoute(url = encodedUrl))
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            AccountInfo(
                modifier = Modifier,
                imageUrl = article.imageUrl,
                accountName = article.userName
            )

            PostDate(date = article.updatedAt)

            Text(
                text = article.title,
                style = QFTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Tags(
                navController = navController,
                tags = article.tags,
                onSearch = onSearch,
                resetState = resetState
            )
            Text(text = "♡ ${article.likesCount}")
        }
    }
}

/** アカウント情報 */
@Composable
private fun AccountInfo(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    accountName: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current

        // todo 表示されない画像データがありそう。要調査（電気通信主任技術者試験（伝搬交換）に挑戦）
        // アイコン
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.noimage),
            modifier = modifier
                .height(40.dp)
                .width(40.dp)
                .aspectRatio(1f)
                .padding(4.dp)
                .aspectRatio(1.0f)
        )

        Spacer(modifier = modifier.width(5.dp))

        // アカウント名
        Text(
            text = "@$accountName",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = QFTypography.labelLarge
        )

        Spacer(modifier = modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.bookmark),
            contentDescription = null,
            modifier = modifier.clickable {
                // todo 保存タブリストに追加。一旦仮でトースト表示
                showToast(context = context, message = "保存しました")
            }
        )
    }
}

/** 投稿日 */
@Composable
private fun PostDate(date: String, modifier: Modifier = Modifier) {
    val parsedDate = OffsetDateTime.parse(date)
    val formattedDate = parsedDate.format(DateFormatter.DATE_FORMAT)

    Text(
        text = "$formattedDate に投稿",
        style = QFTypography.labelMedium
    )
}

object DateFormatter {
    val DATE_FORMAT: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.JAPAN)
}

/** タグ */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Tags(
    navController: NavController,
    tags: List<Tags>?,
    onSearch: (String) -> Unit,
    resetState: () -> Unit,
    modifier: Modifier = Modifier
) {
    tags ?: return
    FlowRow(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        tags.forEach { tag ->
            Tag(
                navController = navController,
                tag = tag,
                onSearch = onSearch,
                resetState = resetState
            )
        }
    }
}

@Composable
private fun Tag(
    navController: NavController,
    tag: Tags,
    onSearch: (String) -> Unit,
    resetState: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(end = 5.dp, bottom = 10.dp)
            .wrapContentWidth()
            .height(20.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(2.dp))
            .padding(2.dp)
            .clickable {
                onSearch.invoke(tag.name)
                resetState.invoke()
                navController.navigateToSearchList(searchListRoute = SearchListRoute(keyword = tag.name))
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag.name,
            style = QFTypography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

/** 記事一覧なし */
@Composable
internal fun NoArticle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.no_article_message))
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountInfoPreview() {
    AccountInfo(
        imageUrl = null,
        accountName = "yanPyanPyanP"
    )
}

@Preview(showBackground = true)
@Composable
private fun TagPreview() {
    Tags(
        navController = NavController(LocalContext.current),
        tags = createTags(),
        onSearch = {},
        resetState = {}
    )
}

@Preview(showBackground = true)
@Composable
internal fun ArticleItemPreview() {
    ArticleItem(
        article = createArticleItem(),
        onSearch = {},
        resetState = {},
        navController = NavController(LocalContext.current)
    )
}

internal fun createTags(): List<Tags> {
    return listOf(Tags("Android", null), Tags("iOS", null), Tags("データ", null))
}

internal fun createArticleItem(): ArticleItemUiModel {
    return ArticleItemUiModel(
        title = "タイトル",
        url = "",
        imageUrl = null,
        userName = "yanP",
        updatedAt = "2023-09-09T00:00:00+09:00",
        tags = createTags(),
        likesCount = 10
    )
}