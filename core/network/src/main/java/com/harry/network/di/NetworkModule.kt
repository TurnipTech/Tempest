package com.harry.network.di

import com.harry.network.client.HttpClient
import com.harry.network.client.HttpRequestExecutor
import com.harry.network.client.KtorHttpClient
import com.harry.network.client.NetworkConfig
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule =
    module {
        single<Json> {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            }
        }

        single<HttpRequestExecutor> {
            HttpRequestExecutor(
                json = get(),
            )
        }

        single<HttpClient> {
            KtorHttpClient(
                config = get(),
                json = get(),
                requestExecutor = get(),
            )
        }

        single { NetworkConfig(timeoutMillis = 10_000L, connectTimeoutMillis = 10_000L) }
    }
