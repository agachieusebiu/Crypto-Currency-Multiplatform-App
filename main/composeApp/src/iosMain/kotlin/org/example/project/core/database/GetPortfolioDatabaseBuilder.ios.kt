package org.example.project.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.core.database.portfolio.PortfolioDatabase
import platform.Foundation.NSHomeDirectory

/**
 * Provides a RoomDatabase.Builder for the PortfolioDatabase on iOS.
 *
 * This function constructs the file path for the database using the NSHomeDirectory and returns a RoomDatabase.Builder configured to use that file path.
 *
 * @return A RoomDatabase.Builder instance for the PortfolioDatabase.
 */
fun getPortfolioDatabaseBuilder(): RoomDatabase.Builder<PortfolioDatabase> {
    val dbFile = NSHomeDirectory() + "/portfolio.db"
    return Room.databaseBuilder<PortfolioDatabase>(
        name = dbFile,
    )
}