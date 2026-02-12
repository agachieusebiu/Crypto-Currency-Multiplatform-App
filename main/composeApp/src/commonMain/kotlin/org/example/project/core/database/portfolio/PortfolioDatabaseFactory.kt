package org.example.project.core.database.portfolio

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Expect declaration for the PortfolioDatabaseCreator object, which is responsible for creating an instance of the PortfolioDatabase.
 * This object will be implemented in platform-specific code to provide the actual database instance.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PortfolioDatabaseCreator : RoomDatabaseConstructor<PortfolioDatabase> {
    override fun initialize(): PortfolioDatabase
}

/**
 * Function to create an instance of the PortfolioDatabase using the provided RoomDatabase.Builder.
 * This function sets up the database with a bundled SQLite driver and configures it to use the IO dispatcher for query operations.
 *
 * @param builder The RoomDatabase.Builder used to construct the PortfolioDatabase instance.
 * @return An instance of PortfolioDatabase configured with the appropriate driver and coroutine context.
 */
fun getPortfolioDatabase(
    builder: RoomDatabase.Builder<PortfolioDatabase>
) : PortfolioDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}