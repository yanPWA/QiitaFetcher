package com.example.qiitafetcher.di

import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.data.repository.ArticlesRepositoryImpl
import com.example.qiitafetcher.data.repository.SaveArticlesRepository
import com.example.qiitafetcher.data.repository.SaveArticlesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository

    @Singleton
    @Binds
    abstract fun bindSaveArticleListRepository(impl: SaveArticlesRepositoryImpl): SaveArticlesRepository
}