package com.example.qiitafetcher.di

import com.example.qiitafetcher.data.AppInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Singleton
    @Binds
    abstract fun bindInterceptor(impl: AppInterceptor): Interceptor
}