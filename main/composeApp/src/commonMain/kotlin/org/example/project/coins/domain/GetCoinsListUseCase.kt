package org.example.project.coins.domain

import org.example.project.coins.data.mapper.toCoinModel
import org.example.project.coins.domain.api.CoinsRemoteDataSource
import org.example.project.coins.domain.model.CoinModel
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.core.domain.map

/**
 * Use case for fetching the list of coins from the remote data source.
 * This use case interacts with the CoinsRemoteDataSource to retrieve the list of coins and maps the response to a list of CoinModel.
 *
 * @property client The remote data source used to fetch the list of coins from the API.
 */
class GetCoinsListUseCase(
    private val client: CoinsRemoteDataSource
) {
    suspend fun execute(): Result<List<CoinModel>, DataError.Remote> {
        return client.getListOfCoins().map { dto ->
            dto.data.coins.map { it.toCoinModel() }
        }
    }
}