package org.example.project.portofolio.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing portfolio coin entities in the local database.
 * This interface defines the methods for inserting, retrieving, and deleting portfolio coin data using Room.
 */
@Dao
interface PortfolioDao {

    @Upsert
    suspend fun insert(portfolioCoinEntity: PortfolioCoinEntity)

    @Query("SELECT * FROM PortfolioCoinEntity ORDER BY timestamp DESC")
    fun getAllOwnedCoins(): Flow<List<PortfolioCoinEntity>>

    @Query("SELECT * FROM PortfolioCoinEntity WHERE coinId = :coinId")
    suspend fun getCoinById(coinId: String): PortfolioCoinEntity?

    @Query("DELETE FROM PortfolioCoinEntity WHERE coinId = :coinId")
    suspend fun deletePortfolioItem(coinId: String)
}