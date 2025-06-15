package com.harry.storage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.harry.storage.DataStoreStorage
import com.harry.storage.Storage
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

val storageModule =
    module {
        single<DataStore<Preferences>> {
            androidContext().dataStore
        }

        single<Json> {
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        }

        single<Storage> {
            DataStoreStorage(
                dataStore = get(),
                json = get(),
            )
        }
    }
