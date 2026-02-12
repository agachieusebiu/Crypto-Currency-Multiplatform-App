package org.example.project.portofolio.presentation

import org.jetbrains.compose.resources.StringResource

/**
 * Data class representing the state of the portfolio screen in the application.
 * This class contains information about the total portfolio value, cash balance, loading state, error messages, and the list of coins in the portfolio.
 *
 * @property portfolioValue The total value of the user's portfolio, formatted as a string.
 * @property cashBalance The current cash balance available to the user, formatted as a string.
 * @property showBuyButton A boolean indicating whether the "Buy" button should be displayed on the UI.
 * @property isLoading A boolean indicating whether the data for the portfolio is currently being loaded.
 * @property error An optional string resource representing any error message that should be displayed to the user.
 * @property coins A list of [UiPortfolioCoinItem] representing the individual coins in the user's portfolio.
 */
data class PortfolioState(
    val portfolioValue: String = "",
    val cashBalance: String = "",
    val showBuyButton: Boolean = false,
    val isLoading: Boolean = false,
    val error: StringResource? = null,
    val coins: List<UiPortfolioCoinItem> = emptyList(),
)
