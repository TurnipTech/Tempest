@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.harry.weather.ui.mapper

import com.harry.location.domain.model.Location
import com.harry.location.domain.usecase.GetStoredLocationUseCase
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.domain.model.WeatherData
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.WeatherViewModel
import com.harry.weather.ui.model.WeatherUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherViewModelTest {
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase = mockk(relaxed = true)
    private val weatherUiMapper: WeatherUiMapper = mockk(relaxed = true)
    private val getStoredLocationUseCase: GetStoredLocationUseCase = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    private val testLocationData =
        com.harry.weather.domain.model.Location(
            latitude = 0.10,
            longitude = 1.10,
            timezone = "Europe/London",
        )

    private val testLocation =
        Location(
            name = "Test City",
            latitude = 0.10,
            longitude = 1.10,
            country = "GB",
        )

    private val testWeatherData =
        WeatherData(
            location = testLocationData,
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
            weeklyForecast = emptyList(),
            timeOfDay = TimeOfDay.DAY,
        )

    @Test
    fun `should be in Loading state initially when fetching current weather`() =
        runTest {
            val expected = WeatherUiState.Loading

            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } coAnswers {
                delay(200)
                Result.success(mockk())
            }

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
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

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

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

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            assertEquals(expected, viewModel.uiState.value)
        }

    @Test
    fun `should be in Success state when getCurrentWeatherUseCase succeeds`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(testWeatherData, "metric", testLocation.name) } returns
                testSuccessState

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            assertEquals(testSuccessState, viewModel.uiState.value)
        }

    @Test
    fun `should call getCurrentWeatherUseCase with location coordinates`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(any(), any(), any()) } returns testSuccessState

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            coVerify(exactly = 1) {
                getCurrentWeatherUseCase.invoke(
                    latitude = testLocation.latitude,
                    longitude = testLocation.longitude,
                    units = "metric",
                    language = "en",
                )
            }
        }

    @Test
    fun `should call weatherUiMapper when getCurrentWeatherUseCase succeeds`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(any(), any(), any()) } returns testSuccessState

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            coVerify(exactly = 1) {
                weatherUiMapper.mapToSuccessState(testWeatherData, "metric", testLocation.name)
            }
        }

    @Test
    fun `should not call weatherUiMapper when getCurrentWeatherUseCase fails`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.failure(Exception("Error"))

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            coVerify(exactly = 0) {
                weatherUiMapper.mapToSuccessState(any(), any(), any())
            }
        }

    @Test
    fun `should use stored location when location parameter is null and stored location exists`() =
        runTest {
            val storedLocation = testLocation
            coEvery { getStoredLocationUseCase.invoke() } returns storedLocation
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(testWeatherData, "metric", storedLocation.name) } returns
                testSuccessState

            val viewModel =
                WeatherViewModel(
                    location = null,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            assertEquals(testSuccessState, viewModel.uiState.value)
            coVerify(exactly = 1) {
                getStoredLocationUseCase.invoke()
            }
            coVerify(exactly = 1) {
                getCurrentWeatherUseCase.invoke(
                    latitude = storedLocation.latitude,
                    longitude = storedLocation.longitude,
                    units = "metric",
                    language = "en",
                )
            }
        }

    @Test
    fun `should be in Error state when location parameter is null and no stored location exists`() =
        runTest {
            val expected =
                WeatherUiState.Error(
                    message = "No location available",
                    canRetry = false,
                )

            coEvery { getStoredLocationUseCase.invoke() } returns null

            val viewModel =
                WeatherViewModel(
                    location = null,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            assertEquals(expected, viewModel.uiState.value)
            coVerify(exactly = 1) {
                getStoredLocationUseCase.invoke()
            }
            coVerify(exactly = 0) {
                getCurrentWeatherUseCase.invoke(any(), any(), any(), any())
            }
            coVerify(exactly = 0) {
                weatherUiMapper.mapToSuccessState(any(), any(), any())
            }
        }

    @Test
    fun `should not call getStoredLocationUseCase when location parameter is provided`() =
        runTest {
            coEvery { getCurrentWeatherUseCase.invoke(any(), any(), any(), any()) } returns
                Result.success(testWeatherData)
            coEvery { weatherUiMapper.mapToSuccessState(any(), any(), any()) } returns testSuccessState

            val viewModel =
                WeatherViewModel(
                    location = testLocation,
                    getCurrentWeatherUseCase = getCurrentWeatherUseCase,
                    weatherUiMapper = weatherUiMapper,
                    getStoredLocationUseCase = getStoredLocationUseCase,
                )
            advanceUntilIdle()

            assertEquals(testSuccessState, viewModel.uiState.value)
            coVerify(exactly = 0) {
                getStoredLocationUseCase.invoke()
            }
        }
}
