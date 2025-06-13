package com.harry.network.client

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface HttpClient {
    suspend fun <T> get(
        serializer: KSerializer<T>,
        endpoint: String,
        params: Map<String, String> = emptyMap(),
    ): Result<T>

    suspend fun <T> post(serializer: KSerializer<T>, endpoint: String, body: Any? = null): Result<T>

    suspend fun <T> put(serializer: KSerializer<T>, endpoint: String, body: Any? = null): Result<T>

    suspend fun <T> delete(serializer: KSerializer<T>, endpoint: String): Result<T>
}

suspend inline fun <reified T> HttpClient.get(endpoint: String, params: Map<String, String> = emptyMap()): Result<T> =
    get(serializer(), endpoint, params)

suspend inline fun <reified T> HttpClient.post(endpoint: String, body: Any? = null): Result<T> =
    post(serializer(), endpoint, body)

suspend inline fun <reified T> HttpClient.put(endpoint: String, body: Any? = null): Result<T> =
    put(serializer(), endpoint, body)

suspend inline fun <reified T> HttpClient.delete(endpoint: String): Result<T> = delete(serializer(), endpoint)
