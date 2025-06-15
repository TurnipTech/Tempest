package com.harry.tempest

import android.app.Application
import com.harry.location.di.locationModule
import com.harry.location.di.searchLocationModule
import com.harry.network.di.networkModule
import com.harry.weather.di.weatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TempestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val locationModule = locationModule(BuildConfig.OPEN_WEATHER_API_KEY)
        val weatherModule = weatherModule(BuildConfig.OPEN_WEATHER_API_KEY)

        startKoin {
            androidLogger()
            androidContext(this@TempestApplication)
            modules(
                networkModule,
                weatherModule,
                locationModule,
                searchLocationModule,
            )
        }
    }
}
