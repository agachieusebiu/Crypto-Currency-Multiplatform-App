package org.example.project.core.network

import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result

/**
 * Extension function for making safe HTTP calls using Ktor's HttpClient.
 * This function executes the provided HTTP request and handles various exceptions that may occur during the network call.
 * It returns a Result object that encapsulates either the successful response or an appropriate error.
 *
 * @param T The expected type of the response body.
 * @param execute A lambda function that performs the HTTP request and returns an HttpResponse.
 * @return A Result object containing either the successful response of type T or a DataError.Remote in case of failure.
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch (_: SocketTimeoutException) {
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (_: UnresolvedAddressException) {
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    return responseToResult(response)
}

/**
 * Converts an HttpResponse to a Result object, handling different HTTP status codes and potential serialization errors.
 *
 * @param T The expected type of the response body.
 * @param response The HttpResponse received from the HTTP request.
 * @return A Result object containing either the successful response of type T or a DataError.Remote in case of failure.
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, DataError.Remote> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (_: Exception) {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }

        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.SERVER)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}

