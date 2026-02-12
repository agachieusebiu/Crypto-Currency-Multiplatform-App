package org.example.project.trade.presentation.common

/**
 * A data class representing a cryptocurrency item in the trade screen of the CoinRoutine application.
 * This class encapsulates the necessary information about a cryptocurrency, including its unique identifier, name, symbol, icon URL, and current price, which are used to display the coin details on the trade screen.
 *
 * @property id The unique identifier of the cryptocurrency.
 * @property name The name of the cryptocurrency.
 * @property symbol The symbol of the cryptocurrency (e.g., BTC for Bitcoin).
 * @property iconUrl The URL of the icon representing the cryptocurrency.
 * @property price The current price of the cryptocurrency.
 */
data class UiTradeCoinItem(
    val id: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val price: Double
)
