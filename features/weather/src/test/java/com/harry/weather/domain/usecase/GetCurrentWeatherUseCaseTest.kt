package com.harry.weather.domain.usecase

import com.harry.weather.data.WeatherRepository
import com.harry.weather.domain.model.WeatherData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCurrentWeatherUseCaseTest {
    private val repository: WeatherRepository = mockk()
    private val useCase = GetCurrentWeatherUseCase(repository)

    private val testWeatherData = mockk<WeatherData>()

    @Test
    fun `should return success when repository returns success`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278
            val units = "metric"
            val language = "en"
            val expectedResult = Result.success(testWeatherData)

            coEvery {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = units,
                    language = language,
                )
            } returns expectedResult

            val result = useCase.invoke(latitude, longitude, units, language)

            assertEquals(expectedResult, result)
            assertTrue(result.isSuccess)
            assertEquals(testWeatherData, result.getOrNull())
        }

    @Test
    fun `should return failure when repository returns failure`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278
            val units = "metric"
            val language = "en"
            val exception = Exception("Network error")
            val expectedResult = Result.failure<WeatherData>(exception)

            coEvery {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = units,
                    language = language,
                )
            } returns expectedResult

            val result = useCase.invoke(latitude, longitude, units, language)

            assertEquals(expectedResult.exceptionOrNull()?.message, result.exceptionOrNull()?.message)
            assertTrue(result.isFailure)
        }

    @Test
    fun `should call repository with default parameters when not provided`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278
            val expectedResult = Result.success(testWeatherData)

            coEvery {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = "metric",
                    language = "en",
                )
            } returns expectedResult

            val result = useCase.invoke(latitude, longitude)

            coVerify(exactly = 1) {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = "metric",
                    language = "en",
                )
            }
            assertTrue(result.isSuccess)
        }

    @Test
    fun `should call repository with provided parameters`() =
        runTest {
            val latitude = 40.7128
            val longitude = -74.0060
            val units = "imperial"
            val language = "es"
            val expectedResult = Result.success(testWeatherData)

            coEvery {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = units,
                    language = language,
                )
            } returns expectedResult

            val result = useCase.invoke(latitude, longitude, units, language)

            coVerify(exactly = 1) {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = units,
                    language = language,
                )
            }
            assertTrue(result.isSuccess)
        }

    @Test
    fun `should pass empty excludeParts list to repository`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278
            val expectedResult = Result.success(testWeatherData)

            coEvery {
                repository.getCurrentWeatherAndForecasts(
                    latitude = any(),
                    longitude = any(),
                    excludeParts = any(),
                    units = any(),
                    language = any(),
                )
            } returns expectedResult

            useCase.invoke(latitude, longitude)

            coVerify(exactly = 1) {
                repository.getCurrentWeatherAndForecasts(
                    latitude = latitude,
                    longitude = longitude,
                    excludeParts = emptyList(),
                    units = "metric",
                    language = "en",
                )
            }
        }

    @Test
    fun `should handle repository exceptions gracefully`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278
            val exception = RuntimeException("Repository error")

            coEvery {
                repository.getCurrentWeatherAndForecasts(
                    latitude = any(),
                    longitude = any(),
                    excludeParts = any(),
                    units = any(),
                    language = any(),
                )
            } throws exception

            try {
                useCase.invoke(latitude, longitude)
                // If we reach here, the exception was not thrown as expected
                assertTrue("Expected exception to be thrown", false)
            } catch (e: RuntimeException) {
                assertEquals("Repository error", e.message)
            }
        }
}
