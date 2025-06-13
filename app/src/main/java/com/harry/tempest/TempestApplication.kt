package com.harry.tempest

import android.app.Application
import com.harry.network.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TempestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TempestApplication)
            modules(
                networkModule,
            )
        }
    }
}
