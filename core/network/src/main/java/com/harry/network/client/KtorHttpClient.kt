package com.harry.network.client

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import io.ktor.client.HttpClient as KtorClientImpl

internal class KtorHttpClient(
    private val config: NetworkConfig,
    private val json: Json,
    private val requestExecutor: HttpRequestExecutor,
) : HttpClient {
    private val ktorClient =
        KtorClientImpl(OkHttp) {
            engine {
                config {
                    connectTimeout(config.connectTimeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
                    readTimeout(config.timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
                }
            }

            install(ContentNegotiation) {
                json(json)
            }
        }

    override suspend fun <T> get(serializer: KSerializer<T>, endpoint: String, params: Map<String, String>): Result<T> =
        requestExecutor.executeRequest(serializer) {
            ktorClient.get {
                url("${config.baseUrl}/$endpoint")
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        }

    override suspend fun <T> post(serializer: KSerializer<T>, endpoint: String, body: Any?): Result<T> =
        requestExecutor.executeRequest(serializer) {
            ktorClient.post {
                url("${config.baseUrl}/$endpoint")
                contentType(ContentType.Application.Json)
                body?.let { setBody(it) }
            }
        }

    override suspend fun <T> put(serializer: KSerializer<T>, endpoint: String, body: Any?): Result<T> =
        requestExecutor.executeRequest(serializer) {
            ktorClient.put {
                url("${config.baseUrl}/$endpoint")
                contentType(ContentType.Application.Json)
                body?.let { setBody(it) }
            }
        }

    override suspend fun <T> delete(serializer: KSerializer<T>, endpoint: String): Result<T> =
        requestExecutor.executeRequest(serializer) {
            ktorClient.delete {
                url("${config.baseUrl}/$endpoint")
            }
        }

    fun close() {
        ktorClient.close()
    }
}
