package org.example.project.coins.domain.model

/**
 * Data class representing the price model in the domain layer of the application.
 * This model includes the price of a coin at a specific timestamp.
 *
 * @property price The price of the coin at the given timestamp.
 * @property timestamp The time at which the price was recorded, represented as a Unix timestamp (milliseconds since epoch).
 */
data class PriceModel(
    val price: Double,
    val timestamp: Long
)