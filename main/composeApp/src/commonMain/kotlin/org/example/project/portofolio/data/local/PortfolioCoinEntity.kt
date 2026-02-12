package org.example.project.portofolio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a coin in the user's portfolio.
 * This class is used to define the structure of the portfolio coin data that will be stored in the local database using Room.
 */
@Entity
data class PortfolioCoinEntity(
    @PrimaryKey val coinId: String,
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val averagePurchasePrice: Double,
    val amountOwned: Double,
    val timestamp: Long,
)
