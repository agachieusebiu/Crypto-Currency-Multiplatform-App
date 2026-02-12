package org.example.project.trade.presentation.common.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import org.example.project.core.util.formatFiat

/**
 * A custom VisualTransformation that formats numeric input as currency while the user is typing.
 * This transformation takes the raw input, checks if it's numeric, and applies currency formatting to it.
 * It also uses a custom OffsetMapping to ensure that the cursor position is maintained correctly as the user types.
 */
private class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.trim()

        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        if (originalText.isNumeric().not()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = formatFiat(
            amount = originalText.toDouble(),
            showDecimal = false
        )

        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText)
        )
    }
}

/**
 * A composable function that provides a remembered instance of the CurrencyVisualTransformation.
 * This function checks if the current mode is inspection mode (e.g., when using Android Studio's preview) and returns a no-op transformation if it is, to avoid issues with formatting in the preview.
 * Otherwise, it returns an instance of CurrencyVisualTransformation for use in text fields that require currency formatting.
 *
 * @return A VisualTransformation that formats input as currency, or a no-op transformation in inspection mode.
 */
@Composable
fun rememberCurrencyVisualTransformation(): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current
    return remember {
        if (inspectionMode) {
            VisualTransformation.None
        } else {
            CurrencyVisualTransformation()
        }
    }
}

private fun String.isNumeric(): Boolean {
    return this.all { char -> char.isDigit() }
}