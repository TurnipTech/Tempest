package com.harry.weather.di

import com.harry.location.domain.model.Location
import com.harry.location.domain.usecase.GetStoredLocationUseCase
import org.junit.Test
import org.koin.test.verify.verify

class WeatherModuleVerificationTest {
    @Test
    fun `verify weather module`() {
        weatherModule("test-api-key").verify(extraTypes = listOf(Location::class, GetStoredLocationUseCase::class))
    }
}
