package org.example.project.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.core.database.portfolio.PortfolioDatabase

/**
 * Provides a RoomDatabase.Builder for the PortfolioDatabase on Android.
 *
 * This function constructs the file path for the database using the context's getDatabasePath method and returns a RoomDatabase.Builder configured to use that file path.
 *
 * @param context The Android context used to access the database path.
 * @return A RoomDatabase.Builder instance for the PortfolioDatabase.
 */
fun getPortfolioDatabaseBuilder(
    context: Context
): RoomDatabase.Builder<PortfolioDatabase> {
    val dbFile = context.getDatabasePath("portfolio.db")
    return Room.databaseBuilder(
        context = context,
        name = dbFile.absolutePath
    )
}