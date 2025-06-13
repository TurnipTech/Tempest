package com.harry.network.exception

/**
 * Base class for all network-related exceptions
 */
sealed class NetworkException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    /**
     * Network connectivity issues (no internet, timeout, etc.)
     */
    class ConnectionException(
        message: String,
        cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /**
     * HTTP errors (4xx, 5xx status codes)
     */
    class HttpException(
        val statusCode: Int,
        message: String,
    ) : NetworkException("HTTP $statusCode: $message")

    /**
     * JSON serialization/deserialization errors
     */
    class SerializationException(
        message: String,
        cause: Throwable? = null,
    ) : NetworkException(message, cause)

    /**
     * Unknown or unexpected errors
     */
    class UnknownException(
        message: String,
        cause: Throwable? = null,
    ) : NetworkException(message, cause)
}
