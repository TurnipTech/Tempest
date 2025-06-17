package com.harry.tempest.di

import com.harry.location.domain.repository.LocationRepository
import org.junit.Test
import org.koin.test.verify.verify

class AppModuleVerificationTest {
    @Test
    fun `verify app module`() {
        appModule.verify(
            extraTypes =
                listOf(
                    LocationRepository::class,
                ),
        )
    }
}
