package com.harry.network

import com.harry.network.client.HttpClient
import com.harry.network.client.KtorHttpClient
import com.harry.network.client.NetworkConfig

object Client {
    fun create(config: NetworkConfig): HttpClient = KtorHttpClient(config)

    fun create(baseUrl: String): HttpClient = create(NetworkConfig(baseUrl = baseUrl))
}
