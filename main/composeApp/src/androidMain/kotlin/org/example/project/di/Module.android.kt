package org.example.project.di

import androidx.room.RoomDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import org.example.project.core.database.getPortfolioDatabaseBuilder
import org.example.project.core.database.portfolio.PortfolioDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module for Android platform-specific dependencies.
 *
 * This module provides the necessary dependencies for the Android platform, including the HTTP client engine and the Room database builder for the PortfolioDatabase.
 */
actual val platformModule = module {
    single<HttpClientEngine> { Android.create() }
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()
}