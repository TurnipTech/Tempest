package com.harry.weather.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeOfDayTest {
    private val testTimeZone = TimeZone.UTC

    @Test
    fun `fromSolarData returns DAY when sunrise and sunset are null`() {
        val result =
            TimeOfDay.fromSolarData(
                currentTime = Instant.fromEpochSeconds(1640700000L), // Any time
                sunrise = null,
                sunset = null,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns DAY when sunrise is null`() {
        val result =
            TimeOfDay.fromSolarData(
                currentTime = Instant.fromEpochSeconds(1640700000L),
                sunrise = null,
                sunset = Instant.fromEpochSeconds(1640714400L),
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns DAY when sunset is null`() {
        val result =
            TimeOfDay.fromSolarData(
                currentTime = Instant.fromEpochSeconds(1640700000L),
                sunrise = Instant.fromEpochSeconds(1640678400L),
                sunset = null,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns DAWN during dawn transition`() {
        // Using a fixed date: 2022-01-01 (1640995200 = 2022-01-01 00:00:00 UTC)
        val baseTime = 1640995200L
        // Sunrise at 6:00 AM UTC
        val sunrise = Instant.fromEpochSeconds(baseTime + 21600L) // +6 hours
        // Sunset at 6:00 PM UTC
        val sunset = Instant.fromEpochSeconds(baseTime + 64800L) // +18 hours
        // Current time at 6:15 AM UTC (sunrise + 15 minutes)
        val currentTime = Instant.fromEpochSeconds(baseTime + 22500L) // +6.25 hours

        val result =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = sunrise,
                sunset = sunset,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.DAWN, result)
    }

    @Test
    fun `fromSolarData returns DUSK during dusk transition`() {
        // Using a fixed date: 2022-01-01 (1640995200 = 2022-01-01 00:00:00 UTC)
        val baseTime = 1640995200L
        // Sunrise at 6:00 AM UTC
        val sunrise = Instant.fromEpochSeconds(baseTime + 21600L) // +6 hours
        // Sunset at 6:00 PM UTC
        val sunset = Instant.fromEpochSeconds(baseTime + 64800L) // +18 hours
        // Current time at 6:15 PM UTC (sunset + 15 minutes)
        val currentTime = Instant.fromEpochSeconds(baseTime + 65700L) // +18.25 hours

        val result =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = sunrise,
                sunset = sunset,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.DUSK, result)
    }

    @Test
    fun `fromSolarData returns DAY during midday`() {
        // Using a fixed date: 2022-01-01 (1640995200 = 2022-01-01 00:00:00 UTC)
        val baseTime = 1640995200L
        // Sunrise at 6:00 AM UTC
        val sunrise = Instant.fromEpochSeconds(baseTime + 21600L) // +6 hours
        // Sunset at 6:00 PM UTC
        val sunset = Instant.fromEpochSeconds(baseTime + 64800L) // +18 hours
        // Current time at 12:00 PM UTC (noon)
        val currentTime = Instant.fromEpochSeconds(baseTime + 43200L) // +12 hours

        val result =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = sunrise,
                sunset = sunset,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.DAY, result)
    }

    @Test
    fun `fromSolarData returns NIGHT during midnight`() {
        // Using a fixed date: 2022-01-01 (1640995200 = 2022-01-01 00:00:00 UTC)
        val baseTime = 1640995200L
        // Sunrise at 6:00 AM UTC
        val sunrise = Instant.fromEpochSeconds(baseTime + 21600L) // +6 hours
        // Sunset at 6:00 PM UTC
        val sunset = Instant.fromEpochSeconds(baseTime + 64800L) // +18 hours
        // Current time at 12:00 AM UTC (midnight)
        val currentTime = Instant.fromEpochSeconds(baseTime) // +0 hours

        val result =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = sunrise,
                sunset = sunset,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.NIGHT, result)
    }

    @Test
    fun `fromSolarData returns NIGHT during late evening`() {
        // Using a fixed date: 2022-01-01 (1640995200 = 2022-01-01 00:00:00 UTC)
        val baseTime = 1640995200L
        // Sunrise at 6:00 AM UTC
        val sunrise = Instant.fromEpochSeconds(baseTime + 21600L) // +6 hours
        // Sunset at 6:00 PM UTC
        val sunset = Instant.fromEpochSeconds(baseTime + 64800L) // +18 hours
        // Current time at 10:00 PM UTC (22:00)
        val currentTime = Instant.fromEpochSeconds(baseTime + 79200L) // +22 hours

        val result =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = sunrise,
                sunset = sunset,
                timeZone = testTimeZone,
            )

        assertEquals(TimeOfDay.NIGHT, result)
    }

    @Test
    fun `fromSolarData handles timezone correctly`() {
        // Test scenario: User in EST (UTC-5), 3:00 PM EST = 8:00 PM UTC
        // Sunrise: 7:00 AM EST = 12:00 PM UTC
        // Sunset: 6:00 PM EST = 11:00 PM UTC
        // Should return DAY, not NIGHT

        val baseTimeUTC = 1640995200L // 2022-01-01 00:00:00 UTC
        val estTimeZone = TimeZone.of("America/New_York")

        // Current time: 3:00 PM EST (8:00 PM UTC)
        val currentTime = Instant.fromEpochSeconds(baseTimeUTC + 20 * 3600) // +20 hours UTC
        // Sunrise: 7:00 AM EST (12:00 PM UTC)
        val sunrise = Instant.fromEpochSeconds(baseTimeUTC + 12 * 3600) // +12 hours UTC
        // Sunset: 6:00 PM EST (11:00 PM UTC)
        val sunset = Instant.fromEpochSeconds(baseTimeUTC + 23 * 3600) // +23 hours UTC

        val result =
            TimeOfDay.fromSolarData(
                currentTime = currentTime,
                sunrise = sunrise,
                sunset = sunset,
                timeZone = estTimeZone,
            )

        // Should be DAY because it's 3 PM local time, even though it's 8 PM UTC
        assertEquals(TimeOfDay.DAY, result)
    }
}
