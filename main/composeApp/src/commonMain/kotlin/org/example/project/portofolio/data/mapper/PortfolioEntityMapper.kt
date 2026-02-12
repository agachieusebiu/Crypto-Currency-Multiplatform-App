package org.example.project.portofolio.data.mapper

import kotlinx.datetime.Clock
import org.example.project.core.domain.coin.Coin
import org.example.project.portofolio.data.local.PortfolioCoinEntity
import org.example.project.portofolio.domain.PortfolioCoinModel

/**
 * Mapper functions for converting between PortfolioCoinEntity and PortfolioCoinModel.
 * These functions facilitate the transformation of data between the local database representation and the domain model used in the application.
 */
fun PortfolioCoinEntity.toPortfolioCoinModel(
    currentPrice: Double
): PortfolioCoinModel {
    return PortfolioCoinModel(
        coin = Coin(
            id = coinId,
            name = name,
            symbol = symbol,
            iconUrl = iconUrl
        ),
        performancePercent = ((currentPrice - averagePurchasePrice) / averagePurchasePrice) * 100,
        averagePurchasePrice = averagePurchasePrice,
        ownedAmountInUnit = amountOwned,
        ownedAmountInFiat = amountOwned * currentPrice
    )
}

fun PortfolioCoinModel.toPortfolioCoinEntity(): PortfolioCoinEntity {
    return PortfolioCoinEntity(
        coinId = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        iconUrl = coin.iconUrl,
        amountOwned = ownedAmountInUnit,
        averagePurchasePrice = averagePurchasePrice,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}