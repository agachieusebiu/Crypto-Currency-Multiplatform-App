package org.example.project.core.domain.coin

/**
 * Data class representing a cryptocurrency coin with its basic information.
 *
 * @property id The unique identifier of the coin.
 * @property name The name of the coin.
 * @property symbol The symbol or ticker of the coin.
 * @property iconUrl The URL to the icon image representing the coin.
 */
data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val iconUrl: String
)