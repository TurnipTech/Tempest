package com.harry.network.client

/**
 * Configuration for network client
 */
data class NetworkConfig(
    val timeoutMillis: Long = 10_000L,
    val connectTimeoutMillis: Long = 10_000L,
)
