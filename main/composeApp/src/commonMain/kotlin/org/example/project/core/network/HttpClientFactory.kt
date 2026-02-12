package org.example.project.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory object for creating instances of Ktor's HttpClient with predefined configurations.
 * This factory provides a method to create an HttpClient with specified engine and common settings such as content negotiation, timeouts, caching, and default headers.
 */
object HttpClientFactory {

    /**
     * You should use your own API key for the CoinRanking API. The "X-Learning-Purposes" header is included to indicate that the requests are made for learning purposes.
     * */
    private val LEARNING_PURPOSES_NAME = "X-Learning-Purposes"
    private val LEARNING_PURPOSES_VALUE = "true"

    fun create(
        engine: HttpClientEngine
    ): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(HttpCache)
            defaultRequest {
                headers {
                    append(
                        LEARNING_PURPOSES_NAME,
                        LEARNING_PURPOSES_VALUE
                    )
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}