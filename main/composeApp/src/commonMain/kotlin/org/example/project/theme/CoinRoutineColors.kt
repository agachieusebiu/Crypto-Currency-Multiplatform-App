package org.example.project.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * This file defines the color palette for the CoinRoutine application, including specific colors for profit and loss indicators.
 * The CoinRoutineColorsPalette data class encapsulates the colors used in the application, and the LocalCoinRoutineColorsPalette provides a way to access these colors throughout the composable hierarchy using composition locals
 */

@Immutable
data class CoinRoutineColorsPalette(
    val profitGreen: Color = Color.Unspecified,
    val lossRed: Color = Color.Unspecified,
)

val ProfitGreenColor = Color(color = 0xFF32de84)
val LossRedColor = Color(color = 0xFFD2122E)

val DarkProfitGreenColor = Color(color = 0xFF32de84)
val DarkLossRedColor = Color(color = 0xFFD2122E)

val LightColorsPalette = CoinRoutineColorsPalette(
    profitGreen = ProfitGreenColor,
    lossRed = LossRedColor,
)

val DarkColorsPalette = CoinRoutineColorsPalette(
    profitGreen = DarkProfitGreenColor,
    lossRed = DarkLossRedColor,
)

val LocalCoinRoutineColorsPalette = compositionLocalOf { CoinRoutineColorsPalette() }