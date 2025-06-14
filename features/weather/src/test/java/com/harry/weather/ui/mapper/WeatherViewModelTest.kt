@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.harry.weather.ui.mapper

import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.WeatherViewModel
import com.harry.weather.ui.model.WeatherUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherViewModelTest {
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase = mockk(relaxed = true)
    private val weatherUiMapper: WeatherUiMapper = mockk(relaxed = true)

    private val testLocation =
        com.harry.weather.domain.model.Location(
            latitude = 0.10,
            longitude = 1.10,
            timezone = "Europe/London",
        )

    private val testWeatherData =
        WeatherData(
            location = testLocation,
            currentWeather = mockk(),
            hourlyForecast = emptyList(),
            dailyForecast = emptyList(),
            alerts = emptyList(),
        )

    private val testSuccessState =
        WeatherUiState.Success(
            weatherData = testWeatherData,
            formattedTemperature = "20°C",
            formattedLocation = "0.1, 1.1",
            weatherDescription = "Sunny",
            lastUpdated = "Updated 10:30",
            todaysHourlyForecast = emptyList(),
        )

    private val viewModel =
        WeatherViewModel(
            getCurrentWeatherUseCase = getCurrentWeatherUseCase,
            weatherUiMapper = weatherUiMapper,
        )

    @Test
    fun `should be in Loading state when fetching current weather`() =
        runTest {
            val expected = WeatherUiState.Loading

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } coAnswers {
                delay(200)
                Result.success(mockk())
            }

            viewModel.loadWeather(0.11, 0.11)
            advanceTimeBy(100)

            assertEquals(expected, viewModel.uiState.value)
        }

    @Test
    fun `should be in Error state when getCurrentWeatherUseCase fails`() =
        runTest {
            val errorMessage = "Network error"
            val expected =
                WeatherUiState.Error(
                    message = errorMessage,
                    canRetry = true,
                )

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.failure(Exception(errorMessage))

            viewModel.loadWeather(0.11, 0.11)
            advanceUntilIdle()

            println("Expected: $expected")
            println("Actual: ${viewModel.uiState.value}")
            assertEquals(expected, viewModel.uiState.value)
        }

    @Test
    fun `should be in Error state with default message when getCurrentWeatherUseCase fails with null message`() =
        runTest {
            val expected =
                WeatherUiState.Error(
                    message = "Failed to load weather data",
                    canRetry = true,
                )

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.failure(Exception())

            viewModel.loadWeather(0.11, 0.11)
            advanceUntilIdle()

            assertEquals(expected, viewModel.uiState.value)
        }

    @Test
    fun `should be in Success state when getCurrentWeatherUseCase succeeds`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(testWeatherData, "metric") } returns testSuccessState

            viewModel.loadWeather(0.11, 0.11)
            advanceUntilIdle()

            assertEquals(testSuccessState, viewModel.uiState.value)
        }

    @Test
    fun `should call getCurrentWeatherUseCase with correct parameters`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278
            val units = "imperial"
            val language = "es"

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(any(), any()) } returns testSuccessState

            viewModel.loadWeather(latitude, longitude, units, language)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                getCurrentWeatherUseCase.invoke(
                    latitude = latitude,
                    longitude = longitude,
                    units = units,
                    language = language,
                )
            }
        }

    @Test
    fun `should call getCurrentWeatherUseCase with default parameters when not provided`() =
        runTest {
            val latitude = 51.5074
            val longitude = -0.1278

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(any(), any()) } returns testSuccessState

            viewModel.loadWeather(latitude, longitude)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                getCurrentWeatherUseCase.invoke(
                    latitude = latitude,
                    longitude = longitude,
                    units = "metric",
                    language = "en",
                )
            }
        }

    @Test
    fun `should call weatherUiMapper when getCurrentWeatherUseCase succeeds`() =
        runTest {
            val units = "imperial"

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(any(), any()) } returns testSuccessState

            viewModel.loadWeather(0.11, 0.11, units)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                weatherUiMapper.mapToSuccessState(testWeatherData, units)
            }
        }

    @Test
    fun `should not call weatherUiMapper when getCurrentWeatherUseCase fails`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.failure(Exception("Error"))

            viewModel.loadWeather(0.11, 0.11)
            advanceUntilIdle()

            coVerify(exactly = 0) {
                weatherUiMapper.mapToSuccessState(any(), any())
            }
        }
}
