package org.example.project

import android.app.Application
import org.example.project.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

/**
 * Custom Application class for the CoinRoutine app.
 *
 * This class initializes the Koin dependency injection framework when the application is created. It extends the Android Application class and implements KoinComponent to allow for dependency injection throughout the app.
 */
class CoinRoutineApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@CoinRoutineApplication)
        }
    }
}