package com.harry.network.client

import com.harry.network.exception.NetworkException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HttpRequestExecutorTest {
    private val json = Json { ignoreUnknownKeys = true }
    private val httpRequestExecutor = HttpRequestExecutor(json)

    @Serializable
    data class TestResponse(
        val message: String,
        val value: Int,
    )

    @Test
    fun `should return failure with HttpException when response status is not successful`() =
        runTest {
            val mockResponse = createMockHttpResponse(HttpStatusCode.BadRequest)

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { mockResponse }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(400, exception.statusCode)
            assertEquals("HTTP 400: Bad Request", exception.message)
        }

    @Test
    fun `should return failure with ConnectionException when UnknownHostException occurs`() =
        runTest {
            val unknownHostException = UnknownHostException("Unable to resolve host")

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { throw unknownHostException }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.ConnectionException>(exception)
            assertEquals("No internet connection", exception.message)
            assertEquals(unknownHostException, exception.cause)
        }

    @Test
    fun `should return failure with ConnectionException when SocketTimeoutException occurs`() =
        runTest {
            val timeoutException = SocketTimeoutException("Read timed out")

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { throw timeoutException }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.ConnectionException>(exception)
            assertEquals("Request timeout", exception.message)
            assertEquals(timeoutException, exception.cause)
        }

    @Test
    fun `should return failure with ConnectionException when ConnectException occurs`() =
        runTest {
            val connectException = ConnectException("Connection refused")

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { throw connectException }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.ConnectionException>(exception)
            assertEquals("Connection failed", exception.message)
            assertEquals(connectException, exception.cause)
        }

    @Test
    fun `should return failure with original exception when unknown exception occurs`() =
        runTest {
            val unknownException = RuntimeException("Something went wrong")

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { throw unknownException }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertEquals(unknownException, exception)
        }

    @Test
    fun `should handle HTTP 500 Internal Server Error as failure`() =
        runTest {
            val mockResponse = createMockHttpResponse(HttpStatusCode.InternalServerError)

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { mockResponse }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(500, exception.statusCode)
            assertEquals("HTTP 500: Internal Server Error", exception.message)
        }

    @Test
    fun `should handle HTTP 404 Not Found as failure`() =
        runTest {
            val mockResponse = createMockHttpResponse(HttpStatusCode.NotFound)

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { mockResponse }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(404, exception.statusCode)
            assertEquals("HTTP 404: Not Found", exception.message)
        }

    @Test
    fun `should handle HTTP 401 Unauthorized as failure`() =
        runTest {
            val mockResponse = createMockHttpResponse(HttpStatusCode.Unauthorized)

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { mockResponse }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(401, exception.statusCode)
            assertEquals("HTTP 401: Unauthorized", exception.message)
        }

    @Test
    fun `should handle generic exception when JSON deserialization fails`() =
        runTest {
            val serializationException = RuntimeException("JSON parsing error")

            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                ) { throw serializationException }

            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertEquals(serializationException, exception)
        }

    private fun createMockHttpResponse(statusCode: HttpStatusCode): HttpResponse =
        mockk<HttpResponse>().apply {
            every { status } returns statusCode
            every { status.isSuccess() } returns statusCode.isSuccess()
            every { status.value } returns statusCode.value
            every { status.description } returns statusCode.description
        }
}
