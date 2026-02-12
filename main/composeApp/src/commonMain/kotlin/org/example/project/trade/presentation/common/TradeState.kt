package org.example.project.trade.presentation.common

import org.jetbrains.compose.resources.StringResource

/**
 * A data class representing the state of a trade screen in the CoinRoutine application.
 * This class encapsulates all the necessary information to represent the current state of the trade screen, including loading status, error messages, available amount for trading, user-entered amount, and the selected coin for trading.
 *
 * @property isLoading A boolean indicating whether a trade operation is currently in progress.
 * @property error An optional StringResource that holds any error message to be displayed on the trade screen.
 * @property availableAmount A string representing the amount of cryptocurrency available for trading.
 * @property amount A string representing the amount of cryptocurrency that the user has entered for trading.
 * @property coin An optional UiTradeCoinItem representing the selected cryptocurrency for trading, which includes details such as name, symbol, and current price.
 */
data class TradeState(
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val availableAmount: String = "",
    val amount: String = "",
    val coin: UiTradeCoinItem? = null
)
