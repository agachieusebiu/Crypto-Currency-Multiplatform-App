package org.example.project.portofolio.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.core.util.formatCoinUnit
import org.example.project.core.util.formatFiat
import org.example.project.core.util.formatPercentage
import org.example.project.core.util.toUiText
import org.example.project.portofolio.domain.PortfolioCoinModel
import org.example.project.portofolio.domain.PortfolioRepository

/**
 * ViewModel for managing the state of the user's portfolio and handling user interactions related to the portfolio.
 *
 * @property portfolioRepository Repository for accessing portfolio data and performing operations on the portfolio.
 * @property coroutineDispatcher Dispatcher for executing asynchronous operations in the ViewModel.
 */
class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _state = MutableStateFlow(PortfolioState(isLoading = true))

    /** Combines multiple flows to create a single state flow for the portfolio screen, handling loading, success, and error states. */
    val state: StateFlow<PortfolioState> = combine(
        _state,
        portfolioRepository.allPortfolioCoinsFlow(),
        portfolioRepository.totalBalanceFlow(),
        portfolioRepository.cashBalanceFlow()
    ) { currentState, portfolioCoinsResponse, totalBalanceResult, cashBalance ->
        when (portfolioCoinsResponse) {
            is Result.Success -> {
                handleSuccessState(
                    currentState = currentState,
                    portfolioCoins = portfolioCoinsResponse.data,
                    totalBalanceResult = totalBalanceResult,
                    cashBalance = cashBalance
                )
            }

            is Result.Error -> {
                handleErrorState(
                    currentState = currentState,
                    error = portfolioCoinsResponse.error
                )
            }
        }
    }.onStart {
        portfolioRepository.initializeBalance()
    }.flowOn(coroutineDispatcher).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PortfolioState(isLoading = true)
    )

    /** Handles the successful retrieval of portfolio coins and updates the state accordingly.
     *
     * @param currentState The current state of the portfolio screen.
     * @param portfolioCoins The list of coins in the user's portfolio.
     * @param totalBalanceResult The result of calculating the total balance of the portfolio.
     * @param cashBalance The current cash balance available to the user.
     * @return An updated [PortfolioState] reflecting the successful retrieval of portfolio data.
     */
    private fun handleSuccessState(
        currentState: PortfolioState,
        portfolioCoins: List<PortfolioCoinModel>,
        totalBalanceResult: Result<Double, DataError>,
        cashBalance: Double
    ): PortfolioState {
        val portfolioValue = when (totalBalanceResult) {
            is Result.Success -> formatFiat(totalBalanceResult.data)
            is Result.Error -> formatFiat(0.0)
        }

        return currentState.copy(
            coins = portfolioCoins.map { it.toUiPortfolioCoinItem() },
            portfolioValue = portfolioValue,
            cashBalance = formatFiat(cashBalance),
            showBuyButton = portfolioCoins.isNotEmpty(),
            isLoading = false,
        )
    }

    /** Handles errors that occur during the retrieval of portfolio data and updates the state accordingly.
     *
     * @param currentState The current state of the portfolio screen.
     * @param error The error that occurred during data retrieval.
     * @return An updated [PortfolioState] reflecting the error state with an appropriate error message.
     */
    private fun handleErrorState(
        currentState: PortfolioState,
        error: DataError
    ): PortfolioState {
        return currentState.copy(
            isLoading = false,
            error = error.toUiText()
        )
    }

    /** Extension function to convert a [PortfolioCoinModel] to a [UiPortfolioCoinItem] for display in the UI.
     *
     * @receiver PortfolioCoinModel The model representing a coin in the user's portfolio.
     * @return A [UiPortfolioCoinItem] containing formatted data for display in the UI.
     */
    private fun PortfolioCoinModel.toUiPortfolioCoinItem(): UiPortfolioCoinItem {
        return UiPortfolioCoinItem(
            id = coin.id,
            name = coin.name,
            iconUrl = coin.iconUrl,
            amountInUnitText = formatCoinUnit(ownedAmountInUnit, coin.symbol),
            amountInFIatText = formatFiat(ownedAmountInFiat),
            performancePercentText = formatPercentage(performancePercent),
            isPositive = performancePercent >= 0
        )
    }
}