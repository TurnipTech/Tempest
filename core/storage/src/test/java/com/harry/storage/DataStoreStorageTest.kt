@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.harry.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DataStoreStorageTest {
    private val dataStore: DataStore<Preferences> = mockk()
    private val json = Json { ignoreUnknownKeys = true }
    private val storage = DataStoreStorage(dataStore, json)

    private val mockPreferences: Preferences = mockk()

    @Serializable
    data class TestData(
        val name: String,
        val age: Int,
    )

    @Before
    fun setUp() {
        every { dataStore.data } returns flowOf(mockPreferences)
    }

    @Test
    fun `get should return stored value when key exists`() =
        runTest {
            val key = "test_key"
            val storedJson = """{"name":"John","age":25}"""
            val expectedData = TestData("John", 25)
            val defaultData = TestData("Default", 0)

            every { mockPreferences[stringPreferencesKey(key)] } returns storedJson

            val result = storage.get(TestData.serializer(), key, defaultData).first()

            assertEquals(expectedData, result)
        }

    @Test
    fun `get should return default value when key does not exist`() =
        runTest {
            val key = "nonexistent_key"
            val defaultData = TestData("Default", 0)

            every { mockPreferences[stringPreferencesKey(key)] } returns null

            val result = storage.get(TestData.serializer(), key, defaultData).first()

            assertEquals(defaultData, result)
        }

    @Test
    fun `get should return default value when deserialization fails`() =
        runTest {
            val key = "test_key"
            val invalidJson = """{"invalid": "json"}"""
            val defaultData = TestData("Default", 0)

            every { mockPreferences[stringPreferencesKey(key)] } returns invalidJson

            val result = storage.get(TestData.serializer(), key, defaultData).first()

            assertEquals(defaultData, result)
        }

    @Test
    fun `getNullable should return stored value when key exists`() =
        runTest {
            val key = "test_key"
            val storedJson = """{"name":"John","age":25}"""
            val expectedData = TestData("John", 25)

            every { mockPreferences[stringPreferencesKey(key)] } returns storedJson

            val result = storage.getNullable(TestData.serializer(), key).first()

            assertEquals(expectedData, result)
        }

    @Test
    fun `getNullable should return null when key does not exist`() =
        runTest {
            val key = "nonexistent_key"

            every { mockPreferences[stringPreferencesKey(key)] } returns null

            val result = storage.getNullable(TestData.serializer(), key).first()

            assertNull(result)
        }

    @Test
    fun `getNullable should return null when deserialization fails`() =
        runTest {
            val key = "test_key"
            val invalidJson = """{"invalid": "json"}"""

            every { mockPreferences[stringPreferencesKey(key)] } returns invalidJson

            val result = storage.getNullable(TestData.serializer(), key).first()

            assertNull(result)
        }

    @Test
    fun `contains should return true when key exists`() =
        runTest {
            val key = "test_key"

            every { mockPreferences.contains(stringPreferencesKey(key)) } returns true

            val result = storage.contains(key).first()

            assertTrue(result)
        }

    @Test
    fun `contains should return false when key does not exist`() =
        runTest {
            val key = "nonexistent_key"

            every { mockPreferences.contains(stringPreferencesKey(key)) } returns false

            val result = storage.contains(key).first()

            assertFalse(result)
        }

    @Test
    fun `get with primitive types should work correctly`() =
        runTest {
            val stringKey = "string_key"
            val stringValue = "test_string"
            val expectedJson = "\"$stringValue\""

            every { mockPreferences[stringPreferencesKey(stringKey)] } returns expectedJson

            val result = storage.get(String.serializer(), stringKey, "").first()

            assertEquals(stringValue, result)
        }

    @Test
    fun `get with integer should work correctly`() =
        runTest {
            val intKey = "int_key"
            val intValue = 42
            val expectedJson = intValue.toString()

            every { mockPreferences[stringPreferencesKey(intKey)] } returns expectedJson

            val result = storage.get(Int.serializer(), intKey, 0).first()

            assertEquals(intValue, result)
        }

    @Test
    fun `get with boolean should work correctly`() =
        runTest {
            val boolKey = "bool_key"
            val boolValue = true
            val expectedJson = boolValue.toString()

            every { mockPreferences[stringPreferencesKey(boolKey)] } returns expectedJson

            val result = storage.get(Boolean.serializer(), boolKey, false).first()

            assertEquals(boolValue, result)
        }
}
