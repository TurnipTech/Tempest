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
import org.junit.Before
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HttpRequestExecutorTest {
    private lateinit var json: Json
    private lateinit var httpRequestExecutor: HttpRequestExecutor

    @Serializable
    data class TestResponse(
        val message: String,
        val value: Int,
    )

    @Before
    fun setup() {
        json = Json { ignoreUnknownKeys = true }
        httpRequestExecutor = HttpRequestExecutor(json)
    }

    @Test
    fun `should return failure with HttpException when response status is not successful`() =
        runTest {
            // Given
            val mockResponse = mockk<HttpResponse>()
            val statusCode = HttpStatusCode.BadRequest

            every { mockResponse.status } returns statusCode
            every { mockResponse.status.isSuccess() } returns false
            every { mockResponse.status.value } returns 400
            every { mockResponse.status.description } returns "Bad Request"

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { mockResponse },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(400, exception.statusCode)
            assertEquals("HTTP 400: Bad Request", exception.message)
        }

    @Test
    fun `should return failure with ConnectionException when UnknownHostException occurs`() =
        runTest {
            // Given
            val unknownHostException = UnknownHostException("Unable to resolve host")

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { throw unknownHostException },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.ConnectionException>(exception)
            assertEquals("No internet connection", exception.message)
            assertEquals(unknownHostException, exception.cause)
        }

    @Test
    fun `should return failure with ConnectionException when SocketTimeoutException occurs`() =
        runTest {
            // Given
            val timeoutException = SocketTimeoutException("Read timed out")

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { throw timeoutException },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.ConnectionException>(exception)
            assertEquals("Request timeout", exception.message)
            assertEquals(timeoutException, exception.cause)
        }

    @Test
    fun `should return failure with ConnectionException when ConnectException occurs`() =
        runTest {
            // Given
            val connectException = ConnectException("Connection refused")

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { throw connectException },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.ConnectionException>(exception)
            assertEquals("Connection failed", exception.message)
            assertEquals(connectException, exception.cause)
        }

    @Test
    fun `should return failure with original exception when unknown exception occurs`() =
        runTest {
            // Given
            val unknownException = RuntimeException("Something went wrong")

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { throw unknownException },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertEquals(unknownException, exception)
        }

    @Test
    fun `should handle HTTP 500 Internal Server Error as failure`() =
        runTest {
            // Given
            val mockResponse = mockk<HttpResponse>()
            val statusCode = HttpStatusCode.InternalServerError

            every { mockResponse.status } returns statusCode
            every { mockResponse.status.isSuccess() } returns false
            every { mockResponse.status.value } returns 500
            every { mockResponse.status.description } returns "Internal Server Error"

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { mockResponse },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(500, exception.statusCode)
            assertEquals("HTTP 500: Internal Server Error", exception.message)
        }

    @Test
    fun `should handle HTTP 404 Not Found as failure`() =
        runTest {
            // Given
            val mockResponse = mockk<HttpResponse>()
            val statusCode = HttpStatusCode.NotFound

            every { mockResponse.status } returns statusCode
            every { mockResponse.status.isSuccess() } returns false
            every { mockResponse.status.value } returns 404
            every { mockResponse.status.description } returns "Not Found"

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { mockResponse },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(404, exception.statusCode)
            assertEquals("HTTP 404: Not Found", exception.message)
        }

    @Test
    fun `should handle HTTP 401 Unauthorized as failure`() =
        runTest {
            // Given
            val mockResponse = mockk<HttpResponse>()
            val statusCode = HttpStatusCode.Unauthorized

            every { mockResponse.status } returns statusCode
            every { mockResponse.status.isSuccess() } returns false
            every { mockResponse.status.value } returns 401
            every { mockResponse.status.description } returns "Unauthorized"

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { mockResponse },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertIs<NetworkException.HttpException>(exception)
            assertEquals(401, exception.statusCode)
            assertEquals("HTTP 401: Unauthorized", exception.message)
        }

    @Test
    fun `should handle generic exception when JSON deserialization fails`() =
        runTest {
            // Given
            val serializationException = RuntimeException("JSON parsing error")

            // When
            val result =
                httpRequestExecutor.executeRequest(
                    TestResponse.serializer(),
                    { throw serializationException },
                )

            // Then
            assert(result.isFailure)
            val exception = result.exceptionOrNull()
            assertEquals(serializationException, exception)
        }
}
