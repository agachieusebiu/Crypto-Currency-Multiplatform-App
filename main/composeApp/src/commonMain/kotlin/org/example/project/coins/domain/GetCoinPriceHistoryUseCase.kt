package org.example.project.coins.domain

import org.example.project.coins.data.mapper.toPriceModel
import org.example.project.coins.domain.api.CoinsRemoteDataSource
import org.example.project.coins.domain.model.PriceModel
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.core.domain.map

/**
 * Use case for fetching the price history of a specific coin by its ID.
 * This use case interacts with the CoinsRemoteDataSource to retrieve the price history and maps the response to a list of PriceModel.
 *
 * @property client The remote data source used to fetch coin price history from the API.
 */
class GetCoinPriceHistoryUseCase(
    private val client: CoinsRemoteDataSource
) {
    suspend fun execute(coinId: String): Result<List<PriceModel>, DataError.Remote> {
        return client.getPriceHistory(coinId).map { dto ->
            dto.data.history.map { it.toPriceModel() }
        }
    }
}