
package com.example.qiitafetcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.qiitafetcher.R
import com.example.qiitafetcher.ui.uiModel.ArticleItemUiModel

//
///**
// * ホームタブ（将来的にタブを増やす）
// */
//@Composable
//internal fun HomeScreen() {
//
//}
//
///**
// * Qiita記事一覧
// */
//@Composable
//private fun ArticleList() {
//
//}
//
///**
// * Qiita記事セル
// */
@Composable
internal fun ArticleItem(modifier: Modifier = Modifier, article: ArticleItemUiModel) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .background(color = Color.Black, shape = RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AccountInfo(
                modifier = Modifier,
                imageUrl = article.imageUrl,
                accountName = article.userName
            )
            Text(
                text = article.title ?: "no title",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            article.tags?.forEach { tag ->
                Text(text = tag?.name ?: "no tag")
            }
            Text(text = article.likesCount.toString())
        }

    }
}

/**
 * アカウント情報
 */
@Composable
private fun AccountInfo(
    modifier: Modifier = Modifier,
    imageUrl: String,
    accountName: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        // アカウント名
        Text(
            text = accountName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

///**
// *
// */
//
//@Preview(showBackground = true)
//@Composable
//private fun AccountInfoPreview() {
//    AccountInfo(imageUrl = "", accountName = "test account")
//}
//
