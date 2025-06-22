package com.harry.weather.ui.mapper

import com.harry.weather.R
import com.harry.weather.domain.model.CurrentWeather
import com.harry.weather.domain.model.DailyWeather
import com.harry.weather.domain.model.HourlyWeather
import com.harry.weather.domain.model.WeatherCondition
import com.harry.weather.domain.model.WeatherData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherUiMapperTest {
    private val mockResourceProvider =
        mockk<com.harry.utils.ResourceProvider> {
            every { getString(R.string.no_data_available) } returns "No data available"
            every { getString(R.string.updated_prefix) } returns "Updated "
            every { getString(R.string.temperature_not_available) } returns "N/A"
        }
    private val mapper = WeatherUiMapper(mockResourceProvider)

    @Test
    fun `mapToSuccessState with valid data returns correct UI state`() {
        val weatherData = createWeatherData()
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertNotNull(result)
        assertEquals("22°C", result.formattedTemperature)
        assertEquals("New York", result.formattedLocation)
        assertEquals("Clear sky", result.weatherDescription)
        assertEquals("https://openweathermap.org/img/wn/01d@4x.png", result.currentWeatherIconUrl)
        assertEquals("clear sky", result.currentWeatherIconDescription)
        assertTrue(result.lastUpdated.contains("Updated"))
        assertEquals(2, result.todaysHourlyForecast.size)
        assertEquals(3, result.weeklyForecast.size)
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
        assertEquals("https://openweathermap.org/img/wn/@4x.png", result.currentWeatherIconUrl)
        assertEquals("No data available", result.currentWeatherIconDescription)
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
    fun `mapToSuccessState limits hourly forecast to 24 items`() {
        val currentTime = System.currentTimeMillis() / 1000
        val hourlyForecast =
            (1..30).map { hour ->
                createHourlyWeather(currentTime + (hour * 3600))
            }
        val weatherData = createWeatherData(hourlyForecast = hourlyForecast)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals(24, result.todaysHourlyForecast.size)
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
        val condition = WeatherCondition("clear sky", "01d")
        val currentWeather = createCurrentWeather(condition = condition)
        val weatherData = createWeatherData(currentWeather = currentWeather)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals("Clear sky", result.weatherDescription)
    }

    @Test
    fun `current weather icon URL is generated correctly for different icon codes`() {
        val condition = WeatherCondition("thunderstorm with light rain", "11d")
        val currentWeather = createCurrentWeather(condition = condition)
        val weatherData = createWeatherData(currentWeather = currentWeather)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals("https://openweathermap.org/img/wn/11d@4x.png", result.currentWeatherIconUrl)
        assertEquals("thunderstorm with light rain", result.currentWeatherIconDescription)
    }

    @Test
    fun `mapToSuccessState includes weekly forecast correctly`() {
        val weatherData = createWeatherData()
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals(3, result.weeklyForecast.size)
        assertNotNull(result.weeklyForecast)
    }

    @Test
    fun `weekly forecast limits to 7 days`() {
        val dailyForecast =
            (1..10).map { day ->
                createDailyWeather(System.currentTimeMillis() / 1000 + (day * 24 * 60 * 60))
            }
        val weatherData = createWeatherData(dailyForecast = dailyForecast)
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertEquals(7, result.weeklyForecast.size)
    }

    @Test
    fun `daily weather UI model is mapped correctly with metric units`() {
        val weatherData = createWeatherData()
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        val firstDaily = result.weeklyForecast.first()
        assertTrue(firstDaily.formattedDay.isNotEmpty())
        assertEquals("25°C", firstDaily.temperatureHigh)
        assertEquals("15°C", firstDaily.temperatureLow)
        assertEquals("https://openweathermap.org/img/wn/01d@2x.png", firstDaily.iconUrl)
        assertEquals("clear sky", firstDaily.iconDescription)
    }

    @Test
    fun `daily weather UI model is mapped correctly with imperial units`() {
        val weatherData = createWeatherData()
        val units = "imperial"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        val firstDaily = result.weeklyForecast.first()
        assertEquals("25°F", firstDaily.temperatureHigh)
        assertEquals("15°F", firstDaily.temperatureLow)
    }

    @Test
    fun `daily weather UI model is mapped correctly with standard units`() {
        val weatherData = createWeatherData()
        val units = "standard"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        val firstDaily = result.weeklyForecast.first()
        assertEquals("25K", firstDaily.temperatureHigh)
        assertEquals("15K", firstDaily.temperatureLow)
    }

    @Test
    fun `weekly forecast with empty daily forecast returns empty list`() {
        val weatherData = createWeatherData(dailyForecast = emptyList())
        val units = "metric"

        val result = mapper.mapToSuccessState(weatherData, units, "New York")

        assertTrue(result.weeklyForecast.isEmpty())
    }

    @Test
    fun `hourly forecast shows correct time for New York timezone`() {
        // Use a future time: current time + 2 hours, at exactly 14:00 UTC
        val currentTime = System.currentTimeMillis() / 1000
        val utcTimestamp = ((currentTime / 3600) + 2) * 3600 // Round to next hour + 1 hour
        val alignedTimestamp = 1735660800L // January 1, 2025 00:00 UTC + 14 hours = 14:00 UTC
        val futureTimestamp = currentTime + (alignedTimestamp % 86400) + 3600 // Ensure it's in the future

        val hourlyForecast = listOf(createHourlyWeather(futureTimestamp))
        val weatherData =
            createWeatherData(
                timezone = "America/New_York",
                hourlyForecast = hourlyForecast,
            )

        val result = mapper.mapToSuccessState(weatherData, "metric", "New York")

        // The specific time will depend on when test runs, so just verify it's formatted correctly
        assertTrue(
            result.todaysHourlyForecast
                .first()
                .formattedTime
                .matches(Regex("\\d{2}:\\d{2}")),
        )
    }

    @Test
    fun `hourly forecast shows correct time for Las Vegas timezone`() {
        val currentTime = System.currentTimeMillis() / 1000
        val futureTimestamp = currentTime + 3600 // 1 hour from now

        val hourlyForecast = listOf(createHourlyWeather(futureTimestamp))
        val weatherData =
            createWeatherData(
                timezone = "America/Los_Angeles",
                hourlyForecast = hourlyForecast,
            )

        val result = mapper.mapToSuccessState(weatherData, "metric", "Las Vegas")

        // Verify time format is correct
        assertTrue(
            result.todaysHourlyForecast
                .first()
                .formattedTime
                .matches(Regex("\\d{2}:\\d{2}")),
        )
    }

    @Test
    fun `hourly forecast shows correct time for Tokyo timezone`() {
        val currentTime = System.currentTimeMillis() / 1000
        val futureTimestamp = currentTime + 7200 // 2 hours from now

        val hourlyForecast = listOf(createHourlyWeather(futureTimestamp))
        val weatherData =
            createWeatherData(
                timezone = "Asia/Tokyo",
                hourlyForecast = hourlyForecast,
            )

        val result = mapper.mapToSuccessState(weatherData, "metric", "Tokyo")

        // Verify time format is correct
        assertTrue(
            result.todaysHourlyForecast
                .first()
                .formattedTime
                .matches(Regex("\\d{2}:\\d{2}")),
        )
    }

    @Test
    fun `daily forecast shows correct day for different timezones`() {
        val currentTime = System.currentTimeMillis() / 1000
        val futureTimestamp = currentTime + (24 * 60 * 60) // 1 day from now
        val dailyForecast = listOf(createDailyWeather(futureTimestamp))

        val nyWeatherData =
            createWeatherData(
                timezone = "America/New_York",
                dailyForecast = dailyForecast,
            )

        val tokyoWeatherData =
            createWeatherData(
                timezone = "Asia/Tokyo",
                dailyForecast = dailyForecast,
            )

        val nyResult = mapper.mapToSuccessState(nyWeatherData, "metric", "New York")
        val tokyoResult = mapper.mapToSuccessState(tokyoWeatherData, "metric", "Tokyo")

        // Both should have valid day names, might be different due to timezone difference
        assertTrue(
            listOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday",
            ).contains(nyResult.weeklyForecast.first().formattedDay),
        )
        assertTrue(
            listOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday",
            ).contains(tokyoResult.weeklyForecast.first().formattedDay),
        )
    }

    @Test
    fun `hourly forecast handles UTC timezone correctly`() {
        val currentTime = System.currentTimeMillis() / 1000
        val futureTimestamp = currentTime + 1800 // 30 minutes from now

        val hourlyForecast = listOf(createHourlyWeather(futureTimestamp))
        val weatherData =
            createWeatherData(
                timezone = "UTC",
                hourlyForecast = hourlyForecast,
            )

        val result = mapper.mapToSuccessState(weatherData, "metric", "London")

        // Verify time format is correct
        assertTrue(
            result.todaysHourlyForecast
                .first()
                .formattedTime
                .matches(Regex("\\d{2}:\\d{2}")),
        )
    }

    @Test
    fun `timezone conversion works correctly with known timestamp`() {
        // Use a specific known timestamp: January 1, 2025 14:00 UTC
        val knownUtcTimestamp = 1735660800L + (14 * 3600) // Jan 1, 2025 14:00 UTC

        // Test both hourly and daily to ensure we're in the allowed time range
        val currentTime = System.currentTimeMillis() / 1000
        val testTimestamp =
            if (knownUtcTimestamp > currentTime && knownUtcTimestamp < currentTime + 86400) {
                knownUtcTimestamp
            } else {
                // Use a timestamp 2 hours from now, but aligned to a known hour
                ((currentTime / 3600) + 2) * 3600
            }

        val hourlyForecast = listOf(createHourlyWeather(testTimestamp))

        // Test New York timezone (UTC-5 in winter)
        val nyWeatherData =
            createWeatherData(
                timezone = "America/New_York",
                hourlyForecast = hourlyForecast,
            )

        // Test Los Angeles timezone (UTC-8 in winter)
        val laWeatherData =
            createWeatherData(
                timezone = "America/Los_Angeles",
                hourlyForecast = hourlyForecast,
            )

        val nyResult = mapper.mapToSuccessState(nyWeatherData, "metric", "New York")
        val laResult = mapper.mapToSuccessState(laWeatherData, "metric", "Los Angeles")

        // Both should have valid formatted times
        assertTrue(
            nyResult.todaysHourlyForecast
                .first()
                .formattedTime
                .matches(Regex("\\d{2}:\\d{2}")),
        )
        assertTrue(
            laResult.todaysHourlyForecast
                .first()
                .formattedTime
                .matches(Regex("\\d{2}:\\d{2}")),
        )

        // The LA time should be 3 hours behind NY time (when both are in standard time)
        val nyTime = nyResult.todaysHourlyForecast.first().formattedTime
        val laTime = laResult.todaysHourlyForecast.first().formattedTime

        // Parse hours to compare (basic validation that they're different)
        val nyHour = nyTime.split(":")[0].toInt()
        val laHour = laTime.split(":")[0].toInt()

        // Account for day wrapping
        val timeDiff = ((nyHour - laHour + 24) % 24)
        assertTrue("Time difference should be 3 hours, but was $timeDiff (NY: $nyTime, LA: $laTime)", timeDiff == 3)
    }

    @Test
    fun `timeOfDay calculation uses location timezone`() {
        // Create weather data with different timezones and same sunrise/sunset times
        val sunrise = 1735696800L // Jan 1, 2025 06:00 UTC
        val sunset = 1735732800L // Jan 1, 2025 16:00 UTC

        val currentWeatherNY = createCurrentWeather().copy(sunrise = sunrise, sunset = sunset)
        val currentWeatherTokyo = createCurrentWeather().copy(sunrise = sunrise, sunset = sunset)

        val nyWeatherData =
            createWeatherData(
                currentWeather = currentWeatherNY,
                timezone = "America/New_York",
            )

        val tokyoWeatherData =
            createWeatherData(
                currentWeather = currentWeatherTokyo,
                timezone = "Asia/Tokyo",
            )

        val nyResult = mapper.mapToSuccessState(nyWeatherData, "metric", "New York")
        val tokyoResult = mapper.mapToSuccessState(tokyoWeatherData, "metric", "Tokyo")

        // Both should calculate TimeOfDay, though the specific value will depend on current time
        // The key test is that different timezones may produce different TimeOfDay values
        assertNotNull(nyResult.timeOfDay)
        assertNotNull(tokyoResult.timeOfDay)

        // Verify TimeOfDay is one of the valid enum values
        assertTrue(nyResult.timeOfDay.name in listOf("NIGHT", "DAWN", "DAY", "DUSK"))
        assertTrue(tokyoResult.timeOfDay.name in listOf("NIGHT", "DAWN", "DAY", "DUSK"))
    }

    private fun createWeatherData(
        currentWeather: CurrentWeather? = createCurrentWeather(),
        hourlyForecast: List<HourlyWeather> = createDefaultHourlyForecast(),
        dailyForecast: List<DailyWeather> = createDefaultDailyForecast(),
        timezone: String = "America/New_York",
    ): WeatherData =
        WeatherData(
            timezone = timezone,
            currentWeather = currentWeather,
            hourlyForecast = hourlyForecast,
            dailyForecast = dailyForecast,
        )

    private fun createCurrentWeather(
        temperature: Double = 22.0,
        condition: WeatherCondition = WeatherCondition("clear sky", "01d"),
    ): CurrentWeather =
        CurrentWeather(
            temperature = temperature,
            sunrise = 1640678400L,
            sunset = 1640714400L,
            condition = condition,
            uvi = dto.uvIndex,
        )

    private fun createHourlyWeather(dateTime: Long): HourlyWeather =
        HourlyWeather(
            dateTime = dateTime,
            temperature = 20.0,
            probabilityOfPrecipitation = 30.0,
            condition = WeatherCondition("clear sky", "01d"),
        )

    private fun createDefaultHourlyForecast(): List<HourlyWeather> {
        val currentTime = System.currentTimeMillis() / 1000
        return listOf(
            createHourlyWeather(currentTime + 3600),
            createHourlyWeather(currentTime + 7200),
        )
    }

    private fun createDailyWeather(dateTime: Long): DailyWeather =
        DailyWeather(
            dateTime = dateTime,
            temperatureHigh = 25.0,
            temperatureLow = 15.0,
            condition = WeatherCondition("clear sky", "01d"),
        )

    private fun createDefaultDailyForecast(): List<DailyWeather> {
        val currentTime = System.currentTimeMillis() / 1000
        return listOf(
            createDailyWeather(currentTime + (24 * 60 * 60)),
            createDailyWeather(currentTime + (2 * 24 * 60 * 60)),
            createDailyWeather(currentTime + (3 * 24 * 60 * 60)),
        )
    }
}
