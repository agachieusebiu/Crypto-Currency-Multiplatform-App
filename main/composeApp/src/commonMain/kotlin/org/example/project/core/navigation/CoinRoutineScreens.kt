package org.example.project.core.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed class representing the different screens in the Coin Routine feature of the application.
 * Each screen is represented as an object or data class, allowing for type-safe navigation and parameter passing.
 */
@Serializable
object Portfolio

@Serializable
object Coins

@Serializable
data class Buy(
    val coinId: String
)

@Serializable
data class Sell(
    val coinId: String
)