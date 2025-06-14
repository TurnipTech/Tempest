package com.harry.weather.data

import com.harry.network.client.HttpClient
import com.harry.weather.data.dto.DailySummaryResponseDto
import com.harry.weather.data.dto.HistoricalWeatherResponseDto
import com.harry.weather.data.dto.WeatherOverviewResponseDto
import com.harry.weather.data.dto.WeatherResponseDto
import com.harry.weather.data.mapper.WeatherMapper
import com.harry.weather.domain.model.DailySummary
import com.harry.weather.domain.model.HistoricalWeather
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.model.WeatherOverview
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenWeatherMapRepositoryTest {
    private val client: HttpClient = mockk(relaxed = true)
    private val mapper: WeatherMapper = mockk(relaxed = true)
    private val apiKey = "expectedApiKey"

    private val repo: OpenWeatherMapRepository =
        OpenWeatherMapRepository(
            mapper = mapper,
            client = client,
            apiKey = apiKey,
        )

    private val testLatitude = 40.7128
    private val testLongitude = -74.0060
    private val testUnits = "metric"
    private val testLanguage = "en"
    private val testDate = "2024-06-14"
    private val testTimestamp = 1609459200L
    private val testTimezone = "Asia/Tokyo"

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
    fun `getHistoricalWeather calls client with correct endpoint and parameters`() =
        runTest {
            val mockDto = mockk<HistoricalWeatherResponseDto>()
            val mockHistoricalWeather = mockk<HistoricalWeather>()
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "dt" to testTimestamp.toString(),
                    "appid" to apiKey,
                    "units" to testUnits,
                    "lang" to testLanguage,
                )

            coEvery { client.get(any<KSerializer<HistoricalWeatherResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToHistoricalWeather(mockDto) } returns mockHistoricalWeather

            val result = repo.getHistoricalWeather(testLatitude, testLongitude, testTimestamp, testUnits, testLanguage)

            coVerify {
                client.get(
                    any<KSerializer<HistoricalWeatherResponseDto>>(),
                    endpoint = "https://api.openweathermap.org/data/3.0/onecall/timemachine",
                    params = expectedParams,
                )
            }

            assertTrue(result.isSuccess)
            assertEquals(mockHistoricalWeather, result.getOrNull())
        }

    @Test
    fun `getHistoricalWeather calls mapper with correct DTO`() =
        runTest {
            val mockDto = mockk<HistoricalWeatherResponseDto>()
            val mockHistoricalWeather = mockk<HistoricalWeather>()

            coEvery { client.get(any<KSerializer<HistoricalWeatherResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToHistoricalWeather(mockDto) } returns mockHistoricalWeather

            repo.getHistoricalWeather(testLatitude, testLongitude, testTimestamp, "imperial", "fr")

            verify { mapper.mapToHistoricalWeather(mockDto) }
        }

    @Test
    fun `getDailyWeatherSummary calls client with correct endpoint and parameters`() =
        runTest {
            val mockDto = mockk<DailySummaryResponseDto>()
            val mockDailySummary = mockk<DailySummary>()
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "date" to testDate,
                    "appid" to apiKey,
                    "units" to testUnits,
                    "tz" to testTimezone,
                )

            coEvery { client.get(any<KSerializer<DailySummaryResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToDailySummary(mockDto) } returns mockDailySummary

            val result = repo.getDailyWeatherSummary(testLatitude, testLongitude, testDate, testUnits, testTimezone)

            coVerify {
                client.get(
                    any<KSerializer<DailySummaryResponseDto>>(),
                    endpoint = "https://api.openweathermap.org/data/3.0/onecall/day_summary",
                    params = expectedParams,
                )
            }

            assertTrue(result.isSuccess)
            assertEquals(mockDailySummary, result.getOrNull())
        }

    @Test
    fun `getDailyWeatherSummary calls mapper with correct DTO`() =
        runTest {
            val mockDto = mockk<DailySummaryResponseDto>()
            val mockDailySummary = mockk<DailySummary>()

            coEvery { client.get(any<KSerializer<DailySummaryResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToDailySummary(mockDto) } returns mockDailySummary

            repo.getDailyWeatherSummary(testLatitude, testLongitude, testDate, "kelvin", null)

            verify { mapper.mapToDailySummary(mockDto) }
        }

    @Test
    fun `getDailyWeatherSummary with null timezone omits timezone parameter`() =
        runTest {
            val mockDto = mockk<DailySummaryResponseDto>()

            coEvery { client.get(any<KSerializer<DailySummaryResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToDailySummary(any()) } returns mockk()

            repo.getDailyWeatherSummary(testLatitude, testLongitude, testDate, testUnits, null)

            coVerify {
                client.get(
                    any<KSerializer<DailySummaryResponseDto>>(),
                    endpoint = any(),
                    params = match { params -> !params.containsKey("tz") },
                )
            }
        }

    @Test
    fun `getWeatherOverview calls client with correct endpoint and parameters`() =
        runTest {
            val mockDto = mockk<WeatherOverviewResponseDto>()
            val mockWeatherOverview = mockk<WeatherOverview>()
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "appid" to apiKey,
                    "units" to testUnits,
                    "date" to testDate,
                )

            coEvery { client.get(any<KSerializer<WeatherOverviewResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToWeatherOverview(mockDto) } returns mockWeatherOverview

            val result = repo.getWeatherOverview(testLatitude, testLongitude, testDate, testUnits)

            coVerify {
                client.get(
                    any<KSerializer<WeatherOverviewResponseDto>>(),
                    endpoint = "https://api.openweathermap.org/data/3.0/onecall/overview",
                    params = expectedParams,
                )
            }

            assertTrue(result.isSuccess)
            assertEquals(mockWeatherOverview, result.getOrNull())
        }

    @Test
    fun `getWeatherOverview calls mapper with correct DTO`() =
        runTest {
            val mockDto = mockk<WeatherOverviewResponseDto>()
            val mockWeatherOverview = mockk<WeatherOverview>()

            coEvery { client.get(any<KSerializer<WeatherOverviewResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToWeatherOverview(mockDto) } returns mockWeatherOverview

            repo.getWeatherOverview(testLatitude, testLongitude, null, "imperial")

            verify { mapper.mapToWeatherOverview(mockDto) }
        }

    @Test
    fun `getWeatherOverview with null date omits date parameter`() =
        runTest {
            val mockDto = mockk<WeatherOverviewResponseDto>()

            coEvery { client.get(any<KSerializer<WeatherOverviewResponseDto>>(), any(), any()) } returns
                Result.success(mockDto)
            every { mapper.mapToWeatherOverview(any()) } returns mockk()

            repo.getWeatherOverview(testLatitude, testLongitude, null, testUnits)

            coVerify {
                client.get(
                    any<KSerializer<WeatherOverviewResponseDto>>(),
                    endpoint = any(),
                    params = match { params -> !params.containsKey("date") },
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

    @Test
    fun `getHistoricalWeather handles client failure`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery {
                client.get(any<KSerializer<HistoricalWeatherResponseDto>>(), any(), any())
            } returns Result.failure(exception)

            val result = repo.getHistoricalWeather(0.0, 0.0, 0L, testUnits, testLanguage)

            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            verify(exactly = 0) { mapper.mapToHistoricalWeather(any()) }
        }

    @Test
    fun `getDailyWeatherSummary handles client failure`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery {
                client.get(any<KSerializer<DailySummaryResponseDto>>(), any(), any())
            } returns Result.failure(exception)

            val result = repo.getDailyWeatherSummary(0.0, 0.0, testDate, testUnits, null)

            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            verify(exactly = 0) { mapper.mapToDailySummary(any()) }
        }

    @Test
    fun `getWeatherOverview handles client failure`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery {
                client.get(any<KSerializer<WeatherOverviewResponseDto>>(), any(), any())
            } returns Result.failure(exception)

            val result = repo.getWeatherOverview(0.0, 0.0, null, testUnits)

            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            verify(exactly = 0) { mapper.mapToWeatherOverview(any()) }
        }
}
