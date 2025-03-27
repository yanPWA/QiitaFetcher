package com.example.qiitafetcher.di

import android.content.Context
import androidx.room.Room
import com.example.qiitafetcher.data.dao.SaveArticleDao
import com.example.qiitafetcher.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideSaveArticleDao(appDatabase: AppDatabase): SaveArticleDao {
        return appDatabase.saveArticleDao()
    }
}