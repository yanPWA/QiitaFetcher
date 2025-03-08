package com.example.qiitafetcher.di

import com.example.qiitafetcher.data.repository.ArticlesRepository
import com.example.qiitafetcher.data.repository.ArticlesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository
}