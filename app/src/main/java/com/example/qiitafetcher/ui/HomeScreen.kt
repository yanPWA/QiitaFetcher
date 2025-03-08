package com.example.qiitafetcher.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.qiitafetcher.R
import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.qiitafetcher.data.model.Tags

//
///**
// * ホームタブ（将来的にタブを増やす）
// */
//@Composable
//internal fun HomeScreen() {
//
//}
//
/**
 * Qiita記事一覧
 */
@Composable
internal fun ArticleList(articles: List<ArticleItemUiModel>, modifier: Modifier = Modifier) {

    // 記事がない場合
    if (articles.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "記事はありません")
        }
    }

    // todo 追加読み込み
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // パディングを追加
    ) {
        item { Spacer(modifier = modifier.height(20.dp)) }

        articles.forEach { article ->
            item {
                ArticleItem(article = article)
            }
        }
    }
}

/**
 * Qiita記事セル
 */
@Composable
internal fun ArticleItem(modifier: Modifier = Modifier, article: ArticleItemUiModel) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
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
    }
}

/** 投稿日 */
@Composable
private fun PostDate(date: String, modifier: Modifier = Modifier) {
     // todo 日付フォーマット（yyyy年mm月dd日）

    Text(
        text = "$date に投稿",
        style = Date
    )
}

/** タグ */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Tags(tags: List<Tags>?, modifier: Modifier = Modifier) {
    tags ?: return
    FlowRow(
        modifier = Modifier.padding(top = 12.dp)
    ) {
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
            style = Tag,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleListPreview() {
    ArticleList(articles = createArticles())
//    ArticleList(articles = emptyList())
}

@Preview(showBackground = true)
@Composable
private fun TagPreview() {
    Tags(tags = createTags())
}

private fun createArticles(): List<ArticleItemUiModel> {
    return List(20) { index ->
        ArticleItemUiModel(
            imageUrl = null,
            userName = "yanP$index",
            updatedAt = "2023-09-01",
            title = "タイトル$index",
            tags = createTags(),
            likesCount = index
        )
    }
}

private fun createTags(): List<Tags> {
    return listOf(Tags("Android", null), Tags("iOS", null), Tags("データ", null))
}