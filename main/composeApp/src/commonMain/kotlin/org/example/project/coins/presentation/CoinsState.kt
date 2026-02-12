package org.example.project.coins.presentation

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource

/**
 * Data class representing the state of the coins screen in the presentation layer of the application.
 * This state includes any error messages, a list of coins to display, and the state of the chart for a selected coin.
 *
 * @property error An optional error message to display if there was an issue loading the coins.
 * @property coins A list of coins to display in the UI, represented as UiCoinListItem objects.
 * @property chartState An optional state for the chart, which includes data for the sparkline and loading status.
 */
@Stable
data class CoinsState(
    val error: StringResource? = null,
    val coins: List<UiCoinListItem> = emptyList(),
    val chartState: UiChartState? = null
)

/**
 * Data class representing the state of the chart for a selected coin in the presentation layer of the application.
 * This state includes the data for the sparkline, a loading status, and the name of the coin being displayed.
 *
 * @property sparkLine A list of double values representing the price history of the coin for the sparkline chart.
 * @property isLoading A boolean indicating whether the chart data is currently being loaded.
 * @property coinName The name of the coin for which the chart is being displayed.
 */
@Stable
data class UiChartState(
    val sparkLine: List<Double> = emptyList(),
    val isLoading: Boolean = false,
    val coinName: String = ""
)