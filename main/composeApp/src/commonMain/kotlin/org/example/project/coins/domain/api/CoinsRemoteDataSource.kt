package org.example.project.coins.domain.api

import org.example.project.coins.data.remote.dto.CoinDetailsResponseDto
import org.example.project.coins.data.remote.dto.CoinPriceHistoryResponseDto
import org.example.project.coins.data.remote.dto.CoinsResponseDto
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result

/**
 * Interface defining the contract for the remote data source that fetches coin-related data from an API.
 * This interface includes methods to retrieve a list of coins, price history for a specific coin, and details of a specific coin by its ID.
 */
interface CoinsRemoteDataSource {
    suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote>

    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>

    suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}