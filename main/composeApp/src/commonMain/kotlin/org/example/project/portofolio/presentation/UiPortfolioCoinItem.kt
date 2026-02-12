package org.example.project.portofolio.presentation

/**
 * Data class representing a coin item in the portfolio UI.
 * This class contains information about the coin, its performance, and the amount owned by the user, formatted for display in the UI.
 *
 * @property id The unique identifier of the coin.
 * @property name The name of the coin.
 * @property iconUrl The URL of the coin's icon image.
 * @property amountInUnitText The amount of the coin owned by the user, formatted as a string (e.g., "0.5 BTC").
 * @property amountInFIatText The value of the owned amount in fiat currency, formatted as a string (e.g., "$25,000").
 * @property performancePercentText The performance percentage of the coin, formatted as a string (e.g., "+10%").
 * @property isPositive A boolean indicating whether the performance is positive (true) or negative (false).
 */
data class UiPortfolioCoinItem(
    val id: String,
    val name: String,
    val iconUrl: String,
    val amountInUnitText: String,
    val amountInFIatText: String,
    val performancePercentText: String,
    val isPositive: Boolean
)
