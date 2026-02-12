package org.example.project.trade.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.coins.domain.GetCoinDetailsUseCase
import org.example.project.core.domain.Result
import org.example.project.core.util.formatFiat
import org.example.project.core.util.toUiText
import org.example.project.portofolio.domain.PortfolioRepository
import org.example.project.trade.domain.SellCoinUseCase
import org.example.project.trade.presentation.common.TradeState
import org.example.project.trade.presentation.common.UiTradeCoinItem
import org.example.project.trade.presentation.mapper.toCoin

/**
 * ViewModel for managing the state of the sell coin screen and handling user interactions related to selling a coin.
 * This class is responsible for fetching the details of the coin being sold, managing the input amount, and performing the sell operation when the user confirms the sale.
 *
 * @property getCoinDetailsUseCase Use case for fetching the details of a specific coin.
 * @property portfolioRepository Repository for accessing portfolio data and performing operations on the portfolio.
 * @property sellCoinUseCase Use case for handling the logic of selling a coin and updating the user's portfolio accordingly.
 * @property coinId The ID of the coin being sold, used to fetch its details and perform the sell operation.
 */
class SellViewModel(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val portfolioRepository: PortfolioRepository,
    private val sellCoinUseCase: SellCoinUseCase,
    private val coinId: String
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    private val _state = MutableStateFlow(TradeState())

    val state = combine(
        _state,
        _amount
    ) { state, amount ->
        state.copy(
            amount = amount
        )
    }.onStart {
        when (val portfolioCoinResponse = portfolioRepository.getPortfolioCoin(coinId)) {
            is Result.Success -> {
                portfolioCoinResponse.data?.ownedAmountInUnit?.let {
                    getCoinDetails(it)
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = portfolioCoinResponse.error.toUiText()
                    )
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TradeState(isLoading = true)
    )

    fun onAmountChanged(amount: String) {
        _amount.value = amount
    }

    private val _events = Channel<SellEvents>(capacity = Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun getCoinDetails(ownedAmountInUnit: Double) {
        when (val coinResponse = getCoinDetailsUseCase.execute(coinId)) {
            is Result.Success -> {
                val availableAmountInFiat = ownedAmountInUnit * coinResponse.data.price
                _state.update {
                    it.copy(
                        coin = UiTradeCoinItem(
                            id = coinResponse.data.coin.id,
                            name = coinResponse.data.coin.name,
                            symbol = coinResponse.data.coin.symbol,
                            iconUrl = coinResponse.data.coin.iconUrl,
                            price = coinResponse.data.price,
                        ),
                        availableAmount = "Available: ${formatFiat(availableAmountInFiat)}"
                    )
                }
            }

            is Result.Error -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = coinResponse.error.toUiText()
                    )
                }
            }
        }
    }

    fun onSellClicked() {
        val tradeCoin = state.value.coin ?: return
        viewModelScope.launch {
            val sellCoinResponse = sellCoinUseCase.sellCoin(
                coin = tradeCoin.toCoin(),
                amountInFiat = _amount.value.toDouble(),
                price = tradeCoin.price
            )

            when (sellCoinResponse) {
                is Result.Success -> {
                    _events.send(SellEvents.SellSuccess)
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = sellCoinResponse.error.toUiText()
                        )
                    }
                }
            }
        }
    }
}

sealed interface SellEvents {
    data object SellSuccess : SellEvents
}