package org.example.project.portofolio.data

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import org.example.project.coins.domain.api.CoinsRemoteDataSource
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.portofolio.data.local.PortfolioDao
import org.example.project.portofolio.data.local.UserBalanceDao
import org.example.project.portofolio.data.local.UserBalanceEntity
import org.example.project.portofolio.data.mapper.toPortfolioCoinEntity
import org.example.project.portofolio.data.mapper.toPortfolioCoinModel
import org.example.project.portofolio.domain.PortfolioCoinModel
import org.example.project.portofolio.domain.PortfolioRepository

/**
 * Implementation of the PortfolioRepository interface that manages the user's cryptocurrency portfolio.
 * This class interacts with both the local database (using Room) and the remote API to provide a complete set of functionalities for managing the portfolio, including retrieving coin data, calculating portfolio value, and updating cash balance.
 */
class PortfolioRepositoryImpl(
    private val portfolioDao: PortfolioDao,
    private val userBalanceDao: UserBalanceDao,
    private val coinsRemoteDataSource: CoinsRemoteDataSource
) : PortfolioRepository {

    /** Initializes the user's cash balance in the local database if it does not already exist.
     * This method checks if a cash balance is present, and if not, it inserts a new balance with a default value of 10,000.0.
     */
    override suspend fun initializeBalance() {
        val currentBalance = userBalanceDao.getCashBalance()
        if (currentBalance == null) {
            userBalanceDao.insertBalance(
                UserBalanceEntity(
                    cashBalance = 10000.0
                )
            )
        }
    }

    /** Retrieves a flow of all portfolio coins owned by the user, along with their current prices.
     * This method first fetches the list of owned coins from the local database, and then retrieves the latest coin data from the remote API to calculate the current value of each coin in the portfolio.
     * If there are no coins in the portfolio, it emits an empty list. If there is an error fetching data from the remote API, it emits an error result.
     *
     * @return A flow that emits a Result containing either a list of PortfolioCoinModel or a DataError.Remote in case of an error.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return portfolioDao.getAllOwnedCoins().flatMapLatest { portfolioCoinEntities ->
            if (portfolioCoinEntities.isEmpty()) {
                flow {
                    emit(Result.Success(emptyList<PortfolioCoinModel>()))
                }
            } else {
                flow {
                    coinsRemoteDataSource.getListOfCoins()
                        .onError { error ->
                            emit(Result.Error(error))
                        }
                        .onSuccess { coinsDto ->
                            val portfolioCoins =
                                portfolioCoinEntities.mapNotNull { portfolioCoinEntity ->
                                    val coin =
                                        coinsDto.data.coins.find { it.uuid == portfolioCoinEntity.coinId }
                                    coin?.let {
                                        portfolioCoinEntity.toPortfolioCoinModel(it.price)
                                    }
                                }
                            emit(Result.Success(portfolioCoins))
                        }
                }
            }
        }.catch {
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
    }

    /** Retrieves a specific coin from the user's portfolio by its ID, along with its current price.
     * This method first fetches the coin data from the remote API to get the latest price, and then checks if the coin exists in the local database. If it does, it returns the coin data along with the current price; if not, it returns null. If there is an error fetching data from the remote API, it returns an error result.
     *
     * @param coinId The ID of the coin to retrieve from the portfolio.
     * @return A Result containing either a PortfolioCoinModel (if found) or null (if not found), or a DataError.Remote in case of an error.
     */
    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
        coinsRemoteDataSource.getCoinById(coinId)
            .onError { error ->
                return Result.Error(error)
            }
            .onSuccess { coinDto ->
                val portfolioCoinEntity = portfolioDao.getCoinById(coinId)
                return if (portfolioCoinEntity != null) {
                    Result.Success(portfolioCoinEntity.toPortfolioCoinModel(coinDto.data.coin.price))
                } else {
                    Result.Success(null)
                }
            }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    /** Saves a coin to the user's portfolio in the local database.
     * This method attempts to insert the provided PortfolioCoinModel into the local database. If the insertion is successful, it returns a success result; if there is an SQLiteException (e.g., due to disk full), it returns an error result indicating that the disk is full.
     *
     * @param portfolioCoinModel The PortfolioCoinModel to be saved to the portfolio.
     * @return An EmptyResult indicating success or a DataError.Local in case of an error.
     */
    override suspend fun savePortfolioCoin(portfolioCoinModel: PortfolioCoinModel): EmptyResult<DataError.Local> {
        try {
            portfolioDao.insert(portfolioCoinModel.toPortfolioCoinEntity())
            return Result.Success(Unit)
        } catch (_: SQLiteException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    /** Removes a coin from the user's portfolio in the local database by its ID.
     * This method deletes the specified coin from the local database using its ID. It does not return any result, as it is assumed that the operation will succeed without issues.
     *
     * @param coinId The ID of the coin to be removed from the portfolio.
     */
    override suspend fun removeCoinFromPortfolio(coinId: String) {
        portfolioDao.deletePortfolioItem(coinId)
    }

    /** Calculates the total value of the user's cryptocurrency portfolio by summing the value of each owned coin based on its current price.
     * This method retrieves the list of owned coins from the local database and then fetches the latest coin data from the remote API to calculate the total value. If there are no coins in the portfolio, it emits a total value of 0.0. If there is an error fetching data from the remote API, it emits an error result.
     *
     * @return A flow that emits a Result containing either the total portfolio value as a Double or a DataError.Remote in case of an error.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        return portfolioDao.getAllOwnedCoins().flatMapLatest { portfolioCoinsEntities ->
            if (portfolioCoinsEntities.isEmpty()) {
                flow {
                    emit(Result.Success(0.0))
                }
            } else {
                flow {
                    val apiResult = coinsRemoteDataSource.getListOfCoins()
                    apiResult.onError { error ->
                        emit(Result.Error(error))
                    }.onSuccess { coinsDto ->
                        val totalValue = portfolioCoinsEntities.sumOf { ownedCoin ->
                            val coinPrice =
                                coinsDto.data.coins.find { it.uuid == ownedCoin.coinId }?.price
                                    ?: 0.0
                            ownedCoin.amountOwned * coinPrice
                        }
                        emit(Result.Success(totalValue))
                    }
                }
            }
        }.catch {
            emit(Result.Error(DataError.Remote.UNKNOWN))
        }
    }

    /** Calculates the total balance of the user's portfolio by combining the cash balance and the total value of the cryptocurrency portfolio.
     * This method combines the cash balance flow and the total portfolio value flow to calculate the overall balance. If there is an error calculating the total portfolio value, it emits an error result; otherwise, it emits the sum of the cash balance and the total portfolio value.
     *
     * @return A flow that emits a Result containing either the total balance as a Double or a DataError.Remote in case of an error.
     */
    override fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>> {
        return combine(
            cashBalanceFlow(),
            calculateTotalPortfolioValue()
        ) { cashBalance, portfolioResult ->
            when (portfolioResult) {
                is Result.Success -> {
                    Result.Success(cashBalance + portfolioResult.data)
                }

                is Result.Error -> {
                    Result.Error(portfolioResult.error)
                }
            }
        }
    }

    /** Retrieves a flow of the user's current cash balance from the local database.
     * This method emits the current cash balance, which is retrieved from the local database. If there is no cash balance found, it emits a default value of 10,000.0.
     *
     * @return A flow that emits the current cash balance as a Double.
     */
    override fun cashBalanceFlow(): Flow<Double> {
        return flow {
            emit(userBalanceDao.getCashBalance() ?: 10000.0)
        }
    }

    /** Updates the user's cash balance in the local database with a new value.
     * This method takes a new balance as input and updates the cash balance in the local database accordingly. It does not return any result, as it is assumed that the operation will succeed without issues.
     *
     * @param newBalance The new cash balance to be updated in the local database.
     */
    override suspend fun updateCashBalance(newBalance: Double) {
        userBalanceDao.updateCashBalance(newBalance)
    }
}