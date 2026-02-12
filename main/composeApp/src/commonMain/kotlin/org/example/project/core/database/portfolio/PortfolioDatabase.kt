package org.example.project.core.database.portfolio

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.project.portofolio.data.local.PortfolioCoinEntity
import org.example.project.portofolio.data.local.PortfolioDao
import org.example.project.portofolio.data.local.UserBalanceDao
import org.example.project.portofolio.data.local.UserBalanceEntity

/**
 * Room database class for the portfolio feature of the application.
 * This database includes entities for portfolio coins and user balances, and provides DAOs for accessing this data.
 *
 * @property portfolioDao The DAO for accessing portfolio coin data.
 * @property userBalanceDao The DAO for accessing user balance data.
 */
@ConstructedBy(PortfolioDatabaseCreator::class)
@Database(
    entities = [PortfolioCoinEntity::class, UserBalanceEntity::class],
    version = 2,
)

abstract class PortfolioDatabase : RoomDatabase() {

    abstract fun portfolioDao(): PortfolioDao

    abstract fun userBalanceDao(): UserBalanceDao
}