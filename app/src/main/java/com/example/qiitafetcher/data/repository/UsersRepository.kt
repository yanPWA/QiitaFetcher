package com.example.qiitafetcher.data.repository

import com.example.qiitafetcher.data.model.Item
import com.example.qiitafetcher.network.NetworkClient
import com.example.qiitafetcher.network.QiitaService

interface ItemsRepository {
    suspend fun getItems(): List<Item>
}

class ItemsRepositoryImpl : ItemsRepository {
    private val api = NetworkClient().retrofit.create(QiitaService.UsersApi::class.java)
    override suspend fun getItems(): List<Item> {
        return api.getItems(
            page = 1,
            perPage = 20
        )
    }
}