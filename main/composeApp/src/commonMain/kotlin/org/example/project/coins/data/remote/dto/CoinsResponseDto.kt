package org.example.project.coins.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for the response of the Coins API endpoint.
 * This DTO is used to parse the JSON response from the API and convert it into a usable format in the application.
 */
@Serializable
data class CoinsResponseDto(
    val data: CoinsListDto
)

/**
 * Data Transfer Object (DTO) for the list of coins contained within the Coins API response.
 * This DTO represents the structure of the list of coins as returned by the API.
 */
@Serializable
data class CoinsListDto(
    val coins: List<CoinItemDto>
)

/**
 * Data Transfer Object (DTO) for the individual coin item contained within the list of coins.
 * This DTO represents the structure of each coin item as returned by the API, including its id, name, symbol, icon URL, price, rank, and change percentage.
 */
@Serializable
data class CoinItemDto(
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val price: Double,
    val rank: Int,
    val change: Double
)