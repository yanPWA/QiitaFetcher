package com.example.qiitafetcher

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.qiitafetcher.data.repository.ItemsRepositoryImpl
import com.example.qiitafetcher.ui.QiitaRout
import com.example.qiitafetcher.ui.theme.QiitaFetcherTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val itemsRepositoryImpl = ItemsRepositoryImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** 通信 */
        lifecycleScope.launch(Dispatchers.IO) {
            itemsRepositoryImpl.getItems().apply {
                Log.d("API", "result -> $this")
            }
        }

        enableEdgeToEdge()
        setContent {
            QiitaFetcherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QiitaRout(
                        name = "Qiita",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
