package com.harry.weather.data

import com.harry.network.client.HttpClient
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.data.mapper.WeatherMapper
import com.harry.weather.domain.model.WeatherData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryTest {
    private val client: HttpClient = mockk(relaxed = true)
    private val mapper: WeatherMapper = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val apiKey = "expectedApiKey"

    private val repo: WeatherRepositoryImpl =
        WeatherRepositoryImpl(
            mapper = mapper,
            client = client,
            apiKey = apiKey,
            ioDispatcher = testDispatcher,
        )

    private val testLatitude = 40.7128
    private val testLongitude = -74.0060
    private val testUnits = "metric"
    private val testLanguage = "en"

    @Test
    fun `getCurrentWeatherAndForecasts calls client with correct endpoint and parameters`() =
        runTest {
            val excludeParts = listOf("minutely", "alerts")
            val mockDto = mockk<WeatherResponseDto>()
            val mockWeatherData = mockk<WeatherData>()
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "appid" to apiKey,
                    "units" to testUnits,
                    "lang" to testLanguage,
                    "exclude" to "minutely,alerts",
                )

            coEvery { client.get(any<KSerializer<WeatherResponseDto>>(), any(), any()) } returns Result.success(mockDto)
            every { mapper.mapToWeatherData(mockDto) } returns mockWeatherData

            val result =
                repo.getCurrentWeatherAndForecasts(
                    testLatitude,
                    testLongitude,
                    excludeParts,
                    testUnits,
                    testLanguage,
                )

            coVerify {
                client.get(
                    any<KSerializer<WeatherResponseDto>>(),
                    endpoint = "https://api.openweathermap.org/data/3.0/onecall",
                    params = expectedParams,
                )
            }

            assertTrue(result.isSuccess)
            assertEquals(mockWeatherData, result.getOrNull())
        }

    @Test
    fun `getCurrentWeatherAndForecasts calls mapper with correct DTO`() =
        runTest {
            val excludeParts = emptyList<String>()
            val mockDto = mockk<WeatherResponseDto>()
            val mockWeatherData = mockk<WeatherData>()

            coEvery { client.get(any<KSerializer<WeatherResponseDto>>(), any(), any()) } returns Result.success(mockDto)
            every { mapper.mapToWeatherData(mockDto) } returns mockWeatherData

            repo.getCurrentWeatherAndForecasts(testLatitude, testLongitude, excludeParts, "imperial", "es")

            verify { mapper.mapToWeatherData(mockDto) }
        }

    @Test
    fun `getCurrentWeatherAndForecasts with empty excludeParts omits exclude parameter`() =
        runTest {
            val excludeParts = emptyList<String>()
            val mockDto = mockk<WeatherResponseDto>()

            coEvery { client.get(any<KSerializer<WeatherResponseDto>>(), any(), any()) } returns Result.success(mockDto)
            every { mapper.mapToWeatherData(any()) } returns mockk()

            repo.getCurrentWeatherAndForecasts(testLatitude, testLongitude, excludeParts, testUnits, testLanguage)

            coVerify {
                client.get(
                    any<KSerializer<WeatherResponseDto>>(),
                    endpoint = any(),
                    params = match { params -> !params.containsKey("exclude") },
                )
            }
        }

    @Test
    fun `getCurrentWeatherAndForecasts handles client failure`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery {
                client.get(any<KSerializer<WeatherResponseDto>>(), any(), any())
            } returns Result.failure(exception)

            val result = repo.getCurrentWeatherAndForecasts(0.0, 0.0, emptyList(), testUnits, testLanguage)

            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            verify(exactly = 0) { mapper.mapToWeatherData(any()) }
        }
}
