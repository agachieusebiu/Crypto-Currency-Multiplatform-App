package org.example.project.core.domain

/**
 * Sealed interface representing different types of data errors that can occur in the application.
 * This interface is used to categorize errors into remote and local errors, allowing for better error handling and reporting.
 */
sealed interface DataError: Error {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        INSUFFICIENT_FUNDS,
        UNKNOWN
    }

}