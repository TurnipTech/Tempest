package com.harry.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataStoreStorage(
    private val dataStore: DataStore<Preferences>,
    private val json: Json = Json { ignoreUnknownKeys = true },
) : Storage {
    override suspend fun <T> put(serializer: KSerializer<T>, key: String, value: T) {
        val jsonString = json.encodeToString(serializer, value)
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = jsonString
        }
    }

    override fun <T> get(serializer: KSerializer<T>, key: String, defaultValue: T): Flow<T> =
        dataStore.data.map { preferences ->
            val jsonString = preferences[stringPreferencesKey(key)]
            if (jsonString != null) {
                try {
                    json.decodeFromString(serializer, jsonString)
                } catch (e: Exception) {
                    defaultValue
                }
            } else {
                defaultValue
            }
        }

    override fun <T> getNullable(serializer: KSerializer<T>, key: String): Flow<T?> =
        dataStore.data.map { preferences ->
            val jsonString = preferences[stringPreferencesKey(key)]
            if (jsonString != null) {
                try {
                    json.decodeFromString(serializer, jsonString)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }

    override suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun contains(key: String): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences.contains(stringPreferencesKey(key))
        }
}
