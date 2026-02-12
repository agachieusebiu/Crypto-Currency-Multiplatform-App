package org.example.project.trade.presentation.common.component

import androidx.compose.ui.text.input.OffsetMapping

/**
 * An implementation of OffsetMapping that maps the offsets between the original unformatted text and the formatted text for currency input.
 * This class is used to ensure that the cursor position is maintained correctly when formatting the input as currency, allowing for a seamless user experience when entering amounts.
 *
 * @param originalText The original unformatted text input by the user.
 * @param formattedText The formatted text that is displayed to the user after applying currency formatting.
 */
class CurrencyOffsetMapping(
    originalText: String,
    formattedText: String
) : OffsetMapping {

    private val originalLength = originalText.length
    private val indexes = findDigitIndexes(originalText, formattedText)

    private fun findDigitIndexes(firstString: String, secondString: String): List<Int> {
        val digitIndexes = mutableListOf<Int>()
        var currentIndex = 0

        for (digit in firstString) {
            val index = secondString.indexOf(digit, currentIndex)
            if (index != -1) {
                digitIndexes.add(index)
                currentIndex = index + 1
            } else {
                return emptyList()
            }
        }

        return digitIndexes
    }

    override fun originalToTransformed(offset: Int): Int {
        if (offset >= originalLength) {
            return indexes.last() + 1
        }
        return indexes[offset]
    }

    override fun transformedToOriginal(offset: Int): Int {
        return indexes.indexOfFirst { it >= offset }.takeIf { it != -1 } ?: originalLength
    }
}