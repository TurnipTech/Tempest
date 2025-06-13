package com.harry.network.di

import org.junit.Test
import org.koin.test.verify.verify

class NetworkModuleVerificationTest {
    @Test
    fun `should verify Koin network module configuration`() {
        networkModule.verify()
    }
}
