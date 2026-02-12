package org.example.project.trade.presentation.sell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import org.example.project.trade.presentation.common.TradeScreen
import org.example.project.trade.presentation.common.TradeType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable function representing the Sell screen in the CoinRoutine application.
 *
 * This screen allows users to sell a specific cryptocurrency. It takes the coin's unique identifier as a parameter and provides a callback for navigation back to the portfolio screen upon a successful sale.
 *
 * @param coinId The unique identifier of the cryptocurrency that the user intends to sell.
 * @param navigateToPortfolio A callback function that is invoked when the sale is successful, allowing the user to navigate back to their portfolio screen.
 */
@Composable
fun SellScreen(
    coinId: String,
    navigateToPortfolio: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = koinViewModel<SellViewModel>(
        parameters = {
            parametersOf(coinId)
        }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    /**
     * A side effect that listens for events emitted by the SellViewModel. When a SellSuccess event is received, it triggers the navigation back to the portfolio screen.
     * This ensures that the UI responds appropriately to changes in the ViewModel's state, allowing for a seamless user experience when a sale is completed successfully.
     */
    LaunchedEffect(viewModel.events) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is SellEvents.SellSuccess -> {
                        navigateToPortfolio()
                    }
                }
            }
        }
    }

    TradeScreen(
        state = state,
        tradeType = TradeType.SELL,
        onAmountChanged = viewModel::onAmountChanged,
        onSubmitClicked = viewModel::onSellClicked
    )
}