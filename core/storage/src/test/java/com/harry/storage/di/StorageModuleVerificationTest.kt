package com.harry.storage.di

import org.junit.Test
import org.koin.test.verify.verify

class StorageModuleVerificationTest {
    @Test
    fun `verify storage module`() {
        storageModule.verify()
    }
}
