package org.example.project.coins.domain

import org.example.project.coins.data.mapper.toCoinModel
import org.example.project.coins.domain.api.CoinsRemoteDataSource
import org.example.project.coins.domain.model.CoinModel
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.core.domain.map

/**
 * Use case for fetching the details of a specific coin by its ID.
 * This use case interacts with the CoinsRemoteDataSource to retrieve the coin details and maps the response to a CoinModel.
 *
 * @property client The remote data source used to fetch coin details from the API.
 */
class GetCoinDetailsUseCase(
    private val client: CoinsRemoteDataSource
) {
    suspend fun execute(coinId: String): Result<CoinModel, DataError.Remote> {
        return client.getCoinById(coinId).map { dto ->
            dto.data.coin.toCoinModel()
        }
    }
}