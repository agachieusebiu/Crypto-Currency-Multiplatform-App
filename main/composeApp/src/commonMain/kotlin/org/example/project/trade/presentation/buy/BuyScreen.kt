package org.example.project.trade.presentation.buy

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
 * Composable function representing the Buy screen in the CoinRoutine application.
 * This screen allows users to buy a specific cryptocurrency by entering the amount they wish to purchase and submitting the transaction.
 *
 * @param coinId The unique identifier of the cryptocurrency that the user wants to buy.
 * @param navigateToPortfolio A callback function that is invoked when the user successfully completes a purchase, allowing navigation back to the portfolio screen.
 */
@Composable
fun BuyScreen(
    coinId: String,
    navigateToPortfolio: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = koinViewModel<BuyViewModel>(
        parameters = {
            parametersOf(coinId)
        }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is BuyEvents.BuySuccess -> {
                        navigateToPortfolio()
                    }
                }
            }
        }
    }

    TradeScreen(
        state = state,
        tradeType = TradeType.BUY,
        onAmountChanged = viewModel::onAmountChanged,
        onSubmitClicked = viewModel::onBuyClicked
    )
}