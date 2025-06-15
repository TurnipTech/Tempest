package com.harry.weather.di

import org.junit.Test
import org.koin.test.verify.verify

class WeatherModuleVerificationTest {
    @Test
    fun `verify weather module`() {
        weatherModule("test-api-key").verify()
    }
}
