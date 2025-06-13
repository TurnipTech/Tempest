package com.harry.network.client

import com.harry.network.exception.NetworkException
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import io.ktor.client.HttpClient as KtorClientImpl

class KtorHttpClient(
    private val config: NetworkConfig,
) : HttpClient {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }

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
        executeRequest(serializer) {
            ktorClient.get {
                url("${config.baseUrl}/$endpoint")
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        }

    override suspend fun <T> post(serializer: KSerializer<T>, endpoint: String, body: Any?): Result<T> =
        executeRequest(serializer) {
            ktorClient.post {
                url("${config.baseUrl}/$endpoint")
                contentType(ContentType.Application.Json)
                body?.let { setBody(it) }
            }
        }

    override suspend fun <T> put(serializer: KSerializer<T>, endpoint: String, body: Any?): Result<T> =
        executeRequest(serializer) {
            ktorClient.put {
                url("${config.baseUrl}/$endpoint")
                contentType(ContentType.Application.Json)
                body?.let { setBody(it) }
            }
        }

    override suspend fun <T> delete(serializer: KSerializer<T>, endpoint: String): Result<T> =
        executeRequest(serializer) {
            ktorClient.delete {
                url("${config.baseUrl}/$endpoint")
            }
        }

    private suspend fun <T> executeRequest(serializer: KSerializer<T>, request: suspend () -> HttpResponse): Result<T> =
        try {
            val response = request()

            if (response.status.isSuccess()) {
                val responseBody = response.body<String>()
                val deserializedResponse = json.decodeFromString(serializer, responseBody)
                Result.success(deserializedResponse)
            } else {
                Result.failure(
                    NetworkException.HttpException(
                        statusCode = response.status.value,
                        message = response.status.description,
                    ),
                )
            }
        } catch (e: UnknownHostException) {
            Result.failure(
                NetworkException.ConnectionException("No internet connection", e),
            )
        } catch (e: SocketTimeoutException) {
            Result.failure(
                NetworkException.ConnectionException("Request timeout", e),
            )
        } catch (e: ConnectException) {
            Result.failure(
                NetworkException.ConnectionException("Connection failed", e),
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    fun close() {
        ktorClient.close()
    }
}
