package com.harry.weather.di

import com.harry.weather.data.OpenWeatherMapRepository
import com.harry.weather.data.WeatherRepository
import org.koin.dsl.module

val weatherModule =
    module {

        factory<WeatherRepository> {
            OpenWeatherMapRepository(
                client = get(),
            )
        }
    }
