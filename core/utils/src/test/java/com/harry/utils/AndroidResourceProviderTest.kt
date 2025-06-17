package com.harry.utils

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AndroidResourceProviderTest {
    private val context: Context = mockk()
    private lateinit var resourceProvider: AndroidResourceProvider

    @Before
    fun setUp() {
        resourceProvider = AndroidResourceProvider(context)
    }

    @Test
    fun `getString delegates to context getString method`() {
        val resourceId = 12345
        val expectedString = "Test String"

        every { context.getString(resourceId) } returns expectedString

        val result = resourceProvider.getString(resourceId)

        assertEquals(expectedString, result)
        verify { context.getString(resourceId) }
    }
}
