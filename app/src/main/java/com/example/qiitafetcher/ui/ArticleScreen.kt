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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.qiitafetcher.R
import com.example.qiitafetcher.domain.model.Tags
import com.example.qiitafetcher.navigation.navigateToDetail
import com.example.qiitafetcher.ui.UiUtils.showToast
import com.example.qiitafetcher.ui.ui_model.ArticleItemUiModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Qiita記事セル
 */
@Composable
internal fun ArticleItem(
    modifier: Modifier = Modifier,
    article: ArticleItemUiModel,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
            .clickable {
                val encodedUrl =
                    URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                navController.navigateToDetail(url = encodedUrl)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            AccountInfo(
                modifier = Modifier,
                imageUrl = article.imageUrl,
                accountName = article.userName
            )

            PostDate(date = article.updatedAt)

            Text(
                text = article.title,
                style = Title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Tags(tags = article.tags)
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
            style = Account
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
        style = Date
    )
}

object DateFormatter {
    val DATE_FORMAT: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.JAPAN)
}

/** タグ */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Tags(tags: List<Tags>?, modifier: Modifier = Modifier) {
    tags ?: return
    FlowRow(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        // todo tag押下で検索できるように
        tags.forEach { tag -> Tag(tag = tag) }
    }
}

@Composable
private fun Tag(tag: Tags, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(end = 5.dp, bottom = 10.dp)
            .wrapContentWidth()
            .height(20.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(2.dp))
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag.name,
            color = Color.White,
            style = com.example.qiitafetcher.ui.Tag,
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
    Tags(tags = createTags())
}

fun createTags(): List<Tags> {
    return listOf(Tags("Android", null), Tags("iOS", null), Tags("データ", null))
}