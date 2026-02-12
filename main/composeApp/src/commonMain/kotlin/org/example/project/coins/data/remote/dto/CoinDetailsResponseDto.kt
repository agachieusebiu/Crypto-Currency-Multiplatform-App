package org.example.project.coins.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for the response of the Coin Details API endpoint.
 * This DTO is used to parse the JSON response from the API and convert it into a usable format in the application.
 */
@Serializable
data class CoinDetailsResponseDto(
    val data: CoinResponseDto
)

/**
 * Data Transfer Object (DTO) for the coin details contained within the Coin Details API response.
 * This DTO represents the structure of the coin details as returned by the API.
 */
@Serializable
data class CoinResponseDto(
    val coin: CoinItemDto
)