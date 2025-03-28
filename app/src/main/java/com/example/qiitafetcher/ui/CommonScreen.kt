package com.example.qiitafetcher.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.qiitafetcher.R

/**
 * 読み込み中画像
 */
@Composable
internal fun LoadingScreen(modifier: Modifier= Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/** エラー画面 */
@Composable
internal fun ErrorScreen(
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // todo いずれボタンカスタム
        TextButton(onClick = { onRefresh.invoke() }) {
            Text(text = stringResource(R.string.retry_message))
        }
    }
}

/**
 * エラーダイアログ
 */
@Composable
internal fun ErrorDialog(message: String, onDismiss: () -> Unit = {}) {
    val showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { /* ダイアログ外をタップしたときの動作 */ },
            title = { Text("通信エラー") },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
}