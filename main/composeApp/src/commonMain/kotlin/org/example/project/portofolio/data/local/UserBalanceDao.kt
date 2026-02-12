package org.example.project.portofolio.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Data Access Object (DAO) for managing user balance entities in the local database.
 * This interface defines the methods for inserting, retrieving, and updating user balance data using Room.
 */
@Dao
interface UserBalanceDao {

    @Query("SELECT cashBalance FROM UserBalanceEntity WHERE id = 1")
    suspend fun getCashBalance(): Double?

    @Upsert
    suspend fun insertBalance(userBalanceEntity: UserBalanceEntity)

    @Query("UPDATE UserBalanceEntity SET cashBalance = :newBalance WHERE id = 1")
    suspend fun updateCashBalance(newBalance: Double)
}