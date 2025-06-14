package com.harry.weather.di

import com.harry.weather.data.WeatherRepository
import com.harry.weather.data.WeatherRepositoryImpl
import org.koin.dsl.module

val weatherModule = module {

    factory<WeatherRepository> {
        WeatherRepositoryImpl(
            client = get()
        )
    }
}
