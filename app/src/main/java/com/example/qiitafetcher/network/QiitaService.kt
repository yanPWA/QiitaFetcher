package com.example.qiitafetcher.network

import com.example.qiitafetcher.data.model.Item
import retrofit2.http.GET
import retrofit2.http.Query

class QiitaService {
    /**
     * 認証中のユーザーの記事の一覧を作成日時の降順で返す
     */
    interface UsersApi {
        @GET("items")
        suspend fun getItems(
            /** ページ番号 */
            @Query("page") page: Int,
            /** 1ページあたりに含まれる要素数 */
            @Query("per_page") perPage: Int
        ): List<Item>
    }
}
