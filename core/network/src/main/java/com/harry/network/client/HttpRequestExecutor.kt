package com.harry.network.client

import com.harry.network.exception.NetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HttpRequestExecutor(
    private val json: Json,
) {
    suspend fun <T> executeRequest(serializer: KSerializer<T>, request: suspend () -> HttpResponse): Result<T> =
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
}
