package org.example.project.coins.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for the response of the Coin Price History API endpoint.
 * This DTO is used to parse the JSON response from the API and convert it into a usable format in the application.
 */
@Serializable
data class CoinPriceHistoryResponseDto(
    val data: CoinPriceHistoryDto
)

/**
 * Data Transfer Object (DTO) for the coin price history contained within the Coin Price History API response.
 * This DTO represents the structure of the coin price history as returned by the API.
 */
@Serializable
data class CoinPriceHistoryDto(
    val history: List<CoinPriceDto>
)

/**
 * Data Transfer Object (DTO) for the individual coin price entry contained within the coin price history.
 * This DTO represents the structure of each price entry as returned by the API, including the price and timestamp.
 */
@Serializable
data class CoinPriceDto(
    val price: Double?,
    val timestamp: Long
)