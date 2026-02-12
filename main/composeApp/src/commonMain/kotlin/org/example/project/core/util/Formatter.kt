package org.example.project.core.util

/**
 * Expect functions for formatting various types of data, such as fiat amounts, coin units, and percentages.
 * These functions are expected to be implemented in platform-specific code (e.g., Android, iOS) to provide appropriate formatting based on the platform's locale and conventions.
 */
expect fun formatFiat(amount: Double, showDecimal: Boolean = true): String

/**
 * Formats a coin unit amount with its symbol. The implementation should handle the appropriate number of decimal places and formatting based on the coin's conventions.
 *
 * @param amount The amount of the coin to format.
 * @param symbol The symbol of the coin (e.g., "BTC", "ETH").
 * @return A formatted string representing the coin amount with its symbol.
 */
expect fun formatCoinUnit(amount: Double, symbol: String): String

/**
 * Formats a percentage amount. The implementation should handle the appropriate number of decimal places and formatting based on the platform's locale and conventions.
 *
 * @param amount The percentage amount to format (e.g., 0.05 for 5%).
 * @return A formatted string representing the percentage amount.
 */
expect fun formatPercentage(amount: Double): String