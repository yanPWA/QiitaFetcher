package com.example.qiitafetcher.data.api

import com.example.qiitafetcher.domain.model.Article
import retrofit2.http.GET
import retrofit2.http.Query

class QiitaService {
    /**
     * 認証中のユーザーの記事の一覧を作成日時の降順で返す
     */
    interface ItemsApi {
        @GET("items")
        suspend fun getItems(
            /** ページ番号 */
            @Query("page") page: Int,
            /** 1ページあたりに含まれる要素数 */
            @Query("per_page") perPage: Int
        ): List<Article>
    }
}
