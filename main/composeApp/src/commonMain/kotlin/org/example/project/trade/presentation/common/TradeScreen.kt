package org.example.project.trade.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.example.project.theme.LocalCoinRoutineColorsPalette
import org.example.project.trade.presentation.common.component.rememberCurrencyVisualTransformation
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function representing the common UI for both Buy and Sell screens in the CoinRoutine application.
 * This screen displays the selected cryptocurrency, allows the user to input the amount they wish to buy or sell, and shows any relevant error messages or available amounts.
 * The UI is designed to be flexible and can be used for both buying and selling by passing the appropriate TradeType and state.
 *
 * @param state The current state of the trade screen, including information about the selected coin, entered amount, available amount, and any error messages.
 * @param tradeType The type of trade being performed (either BUY or SELL), which determines the labels and button colors used in the UI.
 * @param onAmountChanged A callback function that is invoked when the user changes the amount input, allowing the parent composable to update the state accordingly.
 * @param onSubmitClicked A callback function that is invoked when the user clicks the submit button, allowing the parent composable to handle the trade action (e.g., executing a buy or sell transaction).
 */
@Composable
fun TradeScreen(
    state: TradeState,
    tradeType: TradeType,
    onAmountChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                AsyncImage(
                    model = state.coin?.iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(4.dp).clip(CircleShape).size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = state.coin?.name ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(4.dp).testTag("trade_screen_coin_name")
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = when (tradeType) {
                    TradeType.BUY -> "Buy Amount"
                    TradeType.SELL -> "Sell Amount"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            CenteredDollarTextField(
                amountText = state.amount,
                onAmountChange = onAmountChanged
            )

            Text(
                text = state.availableAmount,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(4.dp)
            )

            if (state.error != null) {
                Text(
                    text = stringResource(state.error),
                    style = MaterialTheme.typography.labelLarge,
                    color = LocalCoinRoutineColorsPalette.current.lossRed,
                    modifier = Modifier.padding(4.dp).testTag("trade_error")
                )
            }
        }

        Button(
            onClick = onSubmitClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = when (tradeType) {
                    TradeType.BUY -> LocalCoinRoutineColorsPalette.current.profitGreen
                    TradeType.SELL -> LocalCoinRoutineColorsPalette.current.lossRed
                }
            ),
            contentPadding = PaddingValues(horizontal = 64.dp),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
        ) {
            Text(
                text = when (tradeType) {
                    TradeType.BUY -> "Buy Now"
                    TradeType.SELL -> "Sell Now"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = when (tradeType) {
                    TradeType.BUY -> MaterialTheme.colorScheme.onPrimary
                    TradeType.SELL -> MaterialTheme.colorScheme.onBackground
                }
            )
        }
    }
}

/**
 * A custom composable function that displays a centered text field for entering a dollar amount, with a visual transformation to format the input as currency.
 * The text field automatically focuses when the composable is first displayed, allowing the user to start typing immediately.
 *
 * @param modifier An optional Modifier to be applied to the text field, allowing for customization of layout and appearance.
 * @param amountText The current text value of the amount input, which should be a string representing a numeric value (without the dollar sign).
 * @param onAmountChange A callback function that is invoked when the user changes the text in the input field, allowing the parent composable to update the state accordingly.
 */
@Composable
fun CenteredDollarTextField(
    modifier: Modifier = Modifier,
    amountText: String,
    onAmountChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val currencyVisualTransformation = rememberCurrencyVisualTransformation()

    val displayText = amountText.trimStart('$')

    BasicTextField(
        value = displayText,
        onValueChange = { newValue ->
            val trimmed = newValue.trimStart('0').trim { it.isDigit().not() }
            if (trimmed.isEmpty() || trimmed.toInt() <= 10000) {
                onAmountChange(trimmed)
            }
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .padding(16.dp),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(56.dp).wrapContentWidth()
            ) {
                innerTextField()
            }
        },
        cursorBrush = SolidColor(Color.White),
        visualTransformation = currencyVisualTransformation
    )
}

enum class TradeType {
    BUY, SELL
}