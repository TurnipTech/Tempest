package com.harry.location.di

import org.junit.Test
import org.koin.test.verify.verify

class LocationModuleVerificationTest {
    @Test
    fun `verify location module`() {
        locationModule("test-api-key").verify()
    }
}
