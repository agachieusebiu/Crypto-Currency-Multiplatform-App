package org.example.project.portofolio.domain

import org.example.project.core.domain.coin.Coin

/**
 * Data class representing a coin in the user's portfolio.
 * This class contains information about the coin, its performance, and the amount owned by the user.
 */
data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountInUnit: Double,
    val ownedAmountInFiat: Double,
)
