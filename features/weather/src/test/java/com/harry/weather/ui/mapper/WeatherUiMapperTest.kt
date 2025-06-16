package com.harry.weather.ui.mapper

import com.harry.weather.domain.model.CurrentWeather
import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.Location
import com.harry.weather.domain.model.WeatherCondition
import com.harry.weather.domain.model.WeatherData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherUiMapperTest {
    private val mapper = WeatherUiMapper()

    @Test
    fun `mapToSuccessState with valid data returns correct UI state`() {
        val weatherData = createWeatherData()
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertNotNull(result)
        assertEquals("22°C", result.formattedTemperature)
        assertEquals("New York", result.formattedLocation)
        assertEquals("Clear sky", result.weatherDescription)
        assertTrue(result.lastUpdated.contains("Updated"))
        assertEquals(2, result.todaysHourlyForecast.size)
    }

    @Test
    fun `mapToSuccessState with imperial units formats temperature correctly`() {
        val weatherData = createWeatherData()
        val units = "imperial"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals("22°F", result.formattedTemperature)
    }

    @Test
    fun `mapToSuccessState with standard units formats temperature correctly`() {
        val weatherData = createWeatherData()
        val units = "standard"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals("22K", result.formattedTemperature)
    }

    @Test
    fun `mapToSuccessState with null current weather returns N_A temperature`() {
        val weatherData = createWeatherData(currentWeather = null)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals("N/A", result.formattedTemperature)
        assertEquals("No data available", result.weatherDescription)
    }

    @Test
    fun `mapToSuccessState filters today's hourly forecast correctly`() {
        val currentTime = System.currentTimeMillis() / 1000
        val hourlyForecast =
            listOf(
                createHourlyWeather(currentTime - 3600), // 1 hour ago (should be filtered out)
                createHourlyWeather(currentTime + 3600), // 1 hour from now (should be included)
                createHourlyWeather(currentTime + 7200), // 2 hours from now (should be included)
                createHourlyWeather(currentTime + (25 * 60 * 60)), // 25 hours from now (should be filtered out)
            )
        val weatherData = createWeatherData(hourlyForecast = hourlyForecast)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals(2, result.todaysHourlyForecast.size)
    }

    @Test
    fun `mapToSuccessState limits hourly forecast to 12 items`() {
        val currentTime = System.currentTimeMillis() / 1000
        val hourlyForecast =
            (1..15).map { hour ->
                createHourlyWeather(currentTime + (hour * 3600))
            }
        val weatherData = createWeatherData(hourlyForecast = hourlyForecast)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals(12, result.todaysHourlyForecast.size)
    }

    @Test
    fun `hourly weather UI model is mapped correctly`() {
        val weatherData = createWeatherData()
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        val firstHourly = result.todaysHourlyForecast.first()
        assertTrue(firstHourly.formattedTime.matches(Regex("\\d{2}:\\d{2}")))
        assertEquals("20°", firstHourly.temperature)
        assertEquals("https://openweathermap.org/img/wn/01d@2x.png", firstHourly.iconUrl)
        assertEquals("clear sky", firstHourly.iconDescription)
        assertEquals("30%", firstHourly.precipitationProbability)
    }

    @Test
    fun `weather description is capitalized correctly`() {
        val condition = WeatherCondition(800, "Clear", "clear sky", "01d")
        val currentWeather = createCurrentWeather(condition = condition)
        val weatherData = createWeatherData(currentWeather = currentWeather)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals("Clear sky", result.weatherDescription)
    }

    private fun createWeatherData(
        currentWeather: CurrentWeather? = createCurrentWeather(),
        hourlyForecast: List<HourlyWeather> = createDefaultHourlyForecast(),
    ): WeatherData =
        WeatherData(
            location = Location(40.7128, -74.0060, "America/New_York"),
            currentWeather = currentWeather,
            hourlyForecast = hourlyForecast,
            dailyForecast = emptyList(),
            alerts = emptyList(),
        )

    private fun createCurrentWeather(
        temperature: Double = 22.0,
        condition: WeatherCondition = WeatherCondition(800, "Clear", "clear sky", "01d"),
    ): CurrentWeather =
        CurrentWeather(
            dateTime = System.currentTimeMillis() / 1000,
            sunrise = 1640678400L,
            sunset = 1640714400L,
            temperature = temperature,
            feelsLike = 24.0,
            humidity = 65,
            pressure = 1013,
            windSpeed = 3.2,
            windDirection = 180,
            uvIndex = 5.0,
            cloudiness = 0,
            visibility = 10,
            condition = condition,
        )

    private fun createHourlyWeather(dateTime: Long): HourlyWeather =
        HourlyWeather(
            dateTime = dateTime,
            temperature = 20.0,
            feelsLike = 22.0,
            humidity = 60,
            pressure = 1015,
            windSpeed = 2.8,
            uvIndex = 4.0,
            probabilityOfPrecipitation = 30.0,
            condition = WeatherCondition(800, "Clear", "clear sky", "01d"),
        )

    private fun createDefaultHourlyForecast(): List<HourlyWeather> {
        val currentTime = System.currentTimeMillis() / 1000
        return listOf(
            createHourlyWeather(currentTime + 3600),
            createHourlyWeather(currentTime + 7200),
        )
    }
}
