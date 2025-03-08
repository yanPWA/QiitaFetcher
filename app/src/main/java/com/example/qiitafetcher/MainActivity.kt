package com.example.qiitafetcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.qiitafetcher.data.repository.ArticlesRepositoryImpl
import com.example.qiitafetcher.ui.ArticlesViewModel
import com.example.qiitafetcher.ui.QiitaRout
import com.example.qiitafetcher.ui.theme.QiitaFetcherTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var itemsRepositoryImpl: ArticlesRepositoryImpl
    private val viewModel: ArticlesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            QiitaFetcherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    QiitaRout(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
