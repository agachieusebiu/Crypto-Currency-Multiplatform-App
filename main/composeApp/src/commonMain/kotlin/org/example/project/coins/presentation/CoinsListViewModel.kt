package org.example.project.coins.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.coins.domain.GetCoinPriceHistoryUseCase
import org.example.project.coins.domain.GetCoinsListUseCase
import org.example.project.core.domain.Result
import org.example.project.core.util.formatFiat
import org.example.project.core.util.formatPercentage
import org.example.project.core.util.toUiText

/**
 * ViewModel for managing the state of the coins list and handling user interactions related to coins.
 *
 * @property getCoinsListUseCase Use case for fetching the list of coins.
 * @property getCoinsPriceHistoryUseCase Use case for fetching the price history of a specific coin.
 */
class CoinsListViewModel(
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val getCoinsPriceHistoryUseCase: GetCoinPriceHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoinsState())
    val state = _state
        .onStart {
            getAllCoins()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CoinsState()
        )

    private suspend fun getAllCoins() {
        when (val coinsResponse = getCoinsListUseCase.execute()) {
            is Result.Success -> {
                _state.update {
                    CoinsState(
                        coins = coinsResponse.data.map { coinItem ->
                            UiCoinListItem(
                                id = coinItem.coin.id,
                                name = coinItem.coin.name,
                                iconUrl = coinItem.coin.iconUrl,
                                symbol = coinItem.coin.symbol,
                                formattedPrice = formatFiat(coinItem.price),
                                formattedChange = formatPercentage(coinItem.change),
                                isPositive = coinItem.change >= 0
                            )
                        }
                    )
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        coins = emptyList(),
                        error = coinsResponse.error.toUiText()
                    )
                }
            }
        }
    }

    fun onCoinLongPressed(coinId: String) {
        _state.update {
            it.copy(
                chartState = UiChartState(
                    sparkLine = emptyList(),
                    isLoading = true
                )
            )
        }

        viewModelScope.launch {
            when (val priceHistory = getCoinsPriceHistoryUseCase.execute(coinId)) {
                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            chartState = UiChartState(
                                sparkLine = priceHistory.data.sortedBy { it.timestamp }
                                    .map { it.price },
                                isLoading = false,
                                coinName = _state.value.coins.find { it.id == coinId }?.name.orEmpty()
                            )
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { currentState ->
                        currentState.copy(
                            chartState = UiChartState(
                                sparkLine = emptyList(),
                                isLoading = false,
                                coinName = ""
                            )
                        )
                    }
                }
            }
        }
    }

    fun onDismissChart() {
        _state.update {
            it.copy(
                chartState = null
            )
        }
    }
}