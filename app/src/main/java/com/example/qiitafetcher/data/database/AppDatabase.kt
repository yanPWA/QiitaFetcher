package com.example.qiitafetcher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.qiitafetcher.data.dao.SaveArticleDao
import com.example.qiitafetcher.domain.model.SaveArticle
import com.example.qiitafetcher.domain.model.Tags
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@Database(entities = [SaveArticle::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saveArticleDao(): SaveArticleDao
}

class Converters {
    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        val json = Json {
            // キャメルケースに変換する
            namingStrategy = JsonNamingStrategy.SnakeCase
        }
    }

    @TypeConverter
    fun fromTagsList(value: String): List<Tags>? {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toTagsList(list: List<Tags>): String {
        return json.encodeToString(list)
    }
}