package org.example.project.coins.data.mapper

import org.example.project.coins.data.remote.dto.CoinItemDto
import org.example.project.coins.data.remote.dto.CoinPriceDto
import org.example.project.coins.domain.model.CoinModel
import org.example.project.coins.domain.model.PriceModel
import org.example.project.core.domain.coin.Coin

/**
 * Mapper functions to convert from CoinItemDto and CoinPriceDto to CoinModel and PriceModel respectively.
 */
fun CoinItemDto.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl
    ),
    price = price,
    change = change
)

fun CoinPriceDto.toPriceModel() = PriceModel(
    price = price ?: 0.0,
    timestamp = timestamp
)