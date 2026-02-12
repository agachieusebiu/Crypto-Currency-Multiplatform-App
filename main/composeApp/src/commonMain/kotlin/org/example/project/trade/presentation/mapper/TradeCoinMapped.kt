package org.example.project.trade.presentation.mapper

import org.example.project.core.domain.coin.Coin
import org.example.project.trade.presentation.common.UiTradeCoinItem

/**
 * Extension function to convert a UiTradeCoinItem to a Coin domain model.
 *
 * This function takes a UiTradeCoinItem, which is used in the presentation layer, and maps its properties to create a Coin object that can be used in the domain layer of the application.
 *
 * @receiver UiTradeCoinItem The UI model representing a cryptocurrency item in the trade screen.
 * @return Coin The domain model representing the cryptocurrency, containing its id, name, symbol, and icon URL.
 */
fun UiTradeCoinItem.toCoin() = Coin(
    id = id,
    name = name,
    symbol = symbol,
    iconUrl = iconUrl
)