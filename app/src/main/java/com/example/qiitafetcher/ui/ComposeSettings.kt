package com.example.qiitafetcher.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

val Account: TextStyle
    @Composable get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = dpToSp(14.dp),
        color = Color.White,
        lineHeight = dpToSp(24.dp)
    )

val Date: TextStyle
    @Composable get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = dpToSp(12.dp),
        color = Color.White,
        lineHeight = dpToSp(24.dp)
    )

val Title: TextStyle
    @Composable get() = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = dpToSp(15.dp),
        color = Color.White,
        lineHeight = dpToSp(24.dp)
    )

val Tag: TextStyle
    @Composable get() = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = dpToSp(13.dp),
        color = Color.White,
        lineHeight = dpToSp(24.dp)
    )

/**
 * Dp を Sp に変換する関数。
 */
@Composable
fun dpToSp(dp: Dp): TextUnit {
    val density = LocalDensity.current
    return with(density) { dp.toSp() }
}
