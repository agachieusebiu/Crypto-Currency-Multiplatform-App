package org.example.project.coins.domain.model

import org.example.project.core.domain.coin.Coin

/**
 * Data class representing a coin model in the domain layer of the application.
 * This model includes the coin information, its current price, and the percentage change in price.
 *
 * @property coin The coin information, including its ID, name, symbol, and icon URL.
 * @property price The current price of the coin.
 * @property change The percentage change in the coin's price.
 */
data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double
)