package org.example.project.trade.presentation.buy

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.error_unknown
import org.example.project.trade.presentation.common.TradeScreen
import org.example.project.trade.presentation.common.TradeState
import org.example.project.trade.presentation.common.TradeType
import org.example.project.trade.presentation.common.UiTradeCoinItem
import kotlin.test.Test

/**
 * Test class for the BuyScreen composable, verifying the correct behavior of UI elements based on the provided state and trade type.
 *
 * This test class includes tests to check if the submit button label changes according to the trade type (BUY or SELL), if the coin name is displayed properly, and if error messages are shown when an error state is present. The tests utilize Compose's testing framework to set up the content and assert the presence and properties of UI elements.
 */
class BuyScreenTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkSubmitButtonLabelChangesWithTradeType() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0
            )
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChanged = {},
                onSubmitClicked = {}
            )
        }

        onNodeWithText("Sell Now").assertDoesNotExist()
        onNodeWithText("Buy Now").assertExists()
        onNodeWithText("Buy Now").assertIsDisplayed()

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.SELL,
                onAmountChanged = {},
                onSubmitClicked = {}
            )
        }

        onNodeWithText("Buy Now").assertDoesNotExist()
        onNodeWithText("Sell Now").assertExists()
        onNodeWithText("Sell Now").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkIfCoinNameShowProperlyInBuy() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0
            )
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChanged = {},
                onSubmitClicked = {}
            )
        }

        onNodeWithTag("trade_screen_coin_name").assertExists()
        onNodeWithTag("trade_screen_coin_name").assertTextEquals("Bitcoin")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkErrorIsShownProperly() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0
            ),
            error = Res.string.error_unknown
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChanged = {},
                onSubmitClicked = {}
            )
        }

        onNodeWithTag("trade_error").assertExists()
        onNodeWithTag("trade_error").assertIsDisplayed()
    }
}