package com.harry.weather.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import com.harry.design.TempestTheme
import com.harry.weather.domain.model.TimeOfDay
import com.harry.weather.ui.model.DailyWeatherUiModel
import com.harry.weather.ui.model.HourlyWeatherUiModel
import com.harry.weather.ui.model.UvUiModel
import com.harry.weather.ui.model.WeatherUiState
import com.harry.weather.util.ImageLoadingTestUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ImageLoadingTestUtil.setupFakeImageLoader(context)
    }

    @Test
    fun weatherScreen_loadingState_displaysLoadingSkeleton() {
        WeatherScreenRobot(composeTestRule).apply {
            setLoadingState()
            assertLoadingSkeletonIsDisplayed()
        }
    }

    @Test
    fun weatherScreen_errorState_displaysErrorMessage() {
        val errorMessage = "Network error occurred"

        WeatherScreenRobot(composeTestRule).apply {
            setErrorState(errorMessage)
            assertErrorTextIsDisplayed()
            assertErrorMessageIsDisplayed(errorMessage)
        }
    }

    @Test
    fun weatherScreen_successState_displaysWeatherContent() {
        WeatherScreenRobot(composeTestRule).apply {
            setSuccessState()
            assertTemperatureIsDisplayed("22°C")
            assertLocationIsDisplayed("New York")
            assertDescriptionIsDisplayed("Clear sky")
            assertWeatherIconIsDisplayed("clear sky")
        }
    }

    @Test
    fun weatherScreen_successState_withHourlyForecast_displaysTodaysForecast() {
        WeatherScreenRobot(composeTestRule).apply {
            setSuccessStateWithHourlyForecast()
            scrollToTodaysForecast()
            assertTodaysForecastIsDisplayed()
        }
    }

    @Test
    fun weatherScreen_successState_withWeeklyForecast_displaysWeeklyForecast() {
        WeatherScreenRobot(composeTestRule).apply {
            setSuccessStateWithWeeklyForecast()
            scrollToWeeklyForecast()
            assertWeeklyForecastIsDisplayed()
        }
    }

    @Test
    fun weatherScreen_successState_noForecasts_displaysOnlyCurrentWeather() {
        WeatherScreenRobot(composeTestRule).apply {
            setSuccessStateWithEmptyForecasts()
            assertTemperatureIsDisplayed("22°C")
            assertLocationIsDisplayed("New York")
        }
    }

    @Test
    fun weatherScreen_locationClick_triggersNavigationCallback() {
        WeatherScreenRobot(composeTestRule).apply {
            setSuccessState()
            clickLocation()
            verifyNavigationToSearchWasCalled()
        }
    }

    @Test
    fun weatherScreen_successState_contentIsScrollable() {
        WeatherScreenRobot(composeTestRule).apply {
            setSuccessStateWithAllData()
            assertContentIsScrollable()
        }
    }

    @Test
    fun weatherScreen_errorStateWithRetry_displaysRetryButton() {
        val errorMessage = "Network error occurred"

        WeatherScreenRobot(composeTestRule).apply {
            setErrorStateWithRetry(errorMessage)
            assertErrorTextIsDisplayed()
            assertErrorMessageIsDisplayed(errorMessage)
            assertRetryButtonIsDisplayed()
        }
    }

    @Test
    fun weatherScreen_errorStateWithoutRetry_doesNotDisplayRetryButton() {
        val errorMessage = "No location available"

        WeatherScreenRobot(composeTestRule).apply {
            setErrorStateWithoutRetry(errorMessage)
            assertErrorTextIsDisplayed()
            assertErrorMessageIsDisplayed(errorMessage)
            assertRetryButtonIsNotDisplayed()
        }
    }

    @Test
    fun weatherScreen_retryButtonClick_triggersRetryCallback() {
        val errorMessage = "Network error occurred"

        WeatherScreenRobot(composeTestRule).apply {
            setErrorStateWithRetry(errorMessage)
            clickRetryButton()
            verifyRetryWasCalled()
        }
    }
}

class WeatherScreenRobot(
    private val composeTestRule: ComposeContentTestRule,
) {
    private fun createMockSuccessState(
        temperature: String = "22°C",
        location: String = "New York",
        description: String = "Clear sky",
        iconDescription: String = "clear sky",
        hourlyForecast: List<HourlyWeatherUiModel> = emptyList(),
        weeklyForecast: List<DailyWeatherUiModel> = emptyList(),
        uvIndex: UvUiModel? =
            UvUiModel(
                index = 5,
                level = "Moderate",
                description = "Moderate UV risk",
                uvPercentage = 0.45f,
            ),
    ) = WeatherUiState.Success(
        weatherData = mockk(),
        formattedTemperature = temperature,
        formattedLocation = location,
        weatherDescription = description,
        currentWeatherIconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
        currentWeatherIconDescription = iconDescription,
        lastUpdated = "Updated 10:30",
        todaysHourlyForecast = hourlyForecast,
        weeklyForecast = weeklyForecast,
        timeOfDay = TimeOfDay.DAY,
        uvIndex = uvIndex,
    )

    private fun createMockHourlyForecast() =
        listOf(
            HourlyWeatherUiModel(
                formattedTime = "14:00",
                temperature = "22°",
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                iconDescription = "clear sky",
                precipitationProbability = "20%",
            ),
            HourlyWeatherUiModel(
                formattedTime = "15:00",
                temperature = "25°",
                iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                iconDescription = "few clouds",
                precipitationProbability = "10%",
            ),
        )

    private fun createMockWeeklyForecast() =
        listOf(
            DailyWeatherUiModel(
                formattedDay = "Mon",
                temperatureHigh = "25°C",
                temperatureLow = "15°C",
                iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
                iconDescription = "clear sky",
            ),
            DailyWeatherUiModel(
                formattedDay = "Tue",
                temperatureHigh = "23°C",
                temperatureLow = "13°C",
                iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
                iconDescription = "few clouds",
            ),
        )

    private val onNavigateToSearch: () -> Unit = mockk(relaxed = true)
    private val mockViewModel: WeatherViewModel = mockk(relaxed = true)

    private fun setupScreen(state: WeatherUiState): WeatherScreenRobot {
        every { mockViewModel.uiState } returns MutableStateFlow(state)
        composeTestRule.setContent {
            TempestTheme {
                WeatherScreen(
                    viewModel = mockViewModel,
                    onNavigateToSearch = onNavigateToSearch,
                )
            }
        }
        return this
    }

    fun setLoadingState(): WeatherScreenRobot = setupScreen(WeatherUiState.Loading)

    fun setErrorState(message: String): WeatherScreenRobot = setupScreen(WeatherUiState.Error(message = message))

    fun setErrorStateWithRetry(message: String): WeatherScreenRobot =
        setupScreen(WeatherUiState.Error(message = message, canRetry = true))

    fun setErrorStateWithoutRetry(message: String): WeatherScreenRobot =
        setupScreen(WeatherUiState.Error(message = message, canRetry = false))

    fun setSuccessState(): WeatherScreenRobot = setupScreen(createMockSuccessState())

    fun setSuccessStateWithHourlyForecast(): WeatherScreenRobot =
        setupScreen(createMockSuccessState(hourlyForecast = createMockHourlyForecast()))

    fun setSuccessStateWithWeeklyForecast(): WeatherScreenRobot =
        setupScreen(createMockSuccessState(weeklyForecast = createMockWeeklyForecast()))

    fun setSuccessStateWithEmptyForecasts(): WeatherScreenRobot = setupScreen(createMockSuccessState())

    fun setSuccessStateWithAllData(): WeatherScreenRobot =
        setupScreen(
            createMockSuccessState(
                hourlyForecast = createMockHourlyForecast(),
                weeklyForecast = createMockWeeklyForecast(),
            ),
        )

    fun clickLocation() {
        composeTestRule.onNodeWithText("New York").performClick()
    }

    fun clickRetryButton() {
        composeTestRule.onNodeWithText("Retry").performClick()
    }

    fun scrollToTodaysForecast() {
        composeTestRule.onNodeWithText("24 Hour Forecast").performScrollTo()
    }

    fun scrollToWeeklyForecast() {
        composeTestRule.onNodeWithText("Mon").performScrollTo()
    }

    fun assertLoadingSkeletonIsDisplayed() {
        composeTestRule.onNodeWithTag("weather_loading_skeleton").assertIsDisplayed()
    }

    fun assertErrorTextIsDisplayed() {
        composeTestRule.onNodeWithText("Unable to Load Weather").assertIsDisplayed()
    }

    fun assertErrorMessageIsDisplayed(message: String) {
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    fun assertTemperatureIsDisplayed(temperature: String) {
        composeTestRule.onNodeWithText(temperature).assertIsDisplayed()
    }

    fun assertLocationIsDisplayed(location: String) {
        composeTestRule.onNodeWithText(location).assertIsDisplayed()
    }

    fun assertDescriptionIsDisplayed(description: String) {
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }

    fun assertWeatherIconIsDisplayed(iconDescription: String) {
        composeTestRule.onNodeWithContentDescription(iconDescription).assertIsDisplayed()
    }

    fun assertTodaysForecastIsDisplayed() {
        composeTestRule.onNodeWithText("24 Hour Forecast").assertIsDisplayed()
    }

    fun assertWeeklyForecastIsDisplayed() {
        composeTestRule.onNodeWithText("Mon").assertIsDisplayed()
    }

    fun assertContentIsScrollable() {
        // Test scrollability by scrolling to different sections
        composeTestRule.onNodeWithText("22°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("24 Hour Forecast").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Mon").performScrollTo().assertIsDisplayed()
    }

    fun assertRetryButtonIsDisplayed() {
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    fun assertRetryButtonIsNotDisplayed() {
        composeTestRule.onNodeWithText("Retry").assertIsNotDisplayed()
    }

    fun verifyNavigationToSearchWasCalled() {
        verify { onNavigateToSearch() }
    }

    fun verifyRetryWasCalled() {
        verify { mockViewModel.retry() }
    }
}
