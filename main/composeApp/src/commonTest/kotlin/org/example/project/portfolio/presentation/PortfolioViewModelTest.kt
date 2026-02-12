package org.example.project.portfolio.presentation

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.example.project.core.domain.DataError
import org.example.project.core.util.formatFiat
import org.example.project.core.util.toUiText
import org.example.project.portfolio.data.FakePortfolioRepository
import org.example.project.portofolio.presentation.PortfolioViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for the PortfolioViewModel, verifying the correct behavior of state management and data handling in various scenarios.
 *
 * This test class uses a FakePortfolioRepository to simulate the behavior of the real PortfolioRepository, allowing for testing of both successful data retrieval and error conditions. The tests cover scenarios such as combining state and portfolio coins, updating portfolio value when a coin is added, and handling loading states and error messages on failure.
 */
class PortfolioViewModelTest {

    private lateinit var viewModel: PortfolioViewModel
    private lateinit var portfolioRepository: FakePortfolioRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        portfolioRepository = FakePortfolioRepository()
        viewModel = PortfolioViewModel(
            portfolioRepository = portfolioRepository,
            coroutineDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `State and portfolio coins are properly combined`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.coins.isEmpty())

            val portfolioCoin = FakePortfolioRepository.portfolioCoin
            portfolioRepository.savePortfolioCoin(portfolioCoin)

            awaitItem() // Ignore the first emission
            val updatedState = awaitItem()
            assertTrue(updatedState.coins.isNotEmpty())
            assertFalse(updatedState.isLoading)
            assertEquals(
                FakePortfolioRepository.portfolioCoin.coin.id,
                updatedState.coins.first().id
            )
        }
    }

    @Test
    fun `Portfolio value updated when a coin is added`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(initialState.portfolioValue, formatFiat(10000.0))

            val portfolioCoin = FakePortfolioRepository.portfolioCoin.copy(
                ownedAmountInUnit = 50.0,
                ownedAmountInFiat = 1000.0
            )

            portfolioRepository.savePortfolioCoin(portfolioCoin)
            val updatedState = awaitItem()
            assertEquals(formatFiat(11000.0), updatedState.portfolioValue)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Loading state and error message update on failure`() = runTest {
        portfolioRepository.simulateError()

        viewModel.state.test {
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(DataError.Remote.SERVER.toUiText(), errorState.error)
        }
    }
}