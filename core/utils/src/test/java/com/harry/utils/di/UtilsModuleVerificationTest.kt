package com.harry.utils.di

import org.junit.Test
import org.koin.test.verify.verify

class UtilsModuleVerificationTest {
    @Test
    fun `verify utils module`() {
        utilsModule.verify()
    }
}
