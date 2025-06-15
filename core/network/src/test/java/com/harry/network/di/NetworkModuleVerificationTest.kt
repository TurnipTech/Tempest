package com.harry.network.di

import org.junit.Test
import org.koin.test.verify.verify

class NetworkModuleVerificationTest {
    @Test
    fun `verify network module`() {
        networkModule.verify()
    }
}
