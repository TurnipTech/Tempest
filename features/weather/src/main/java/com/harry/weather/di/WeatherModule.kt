package com.harry.weather.di

import com.harry.weather.BuildConfig
import com.harry.weather.data.OpenWeatherMapRepository
import com.harry.weather.data.WeatherRepository
import com.harry.weather.data.mapper.WeatherMapper
import org.koin.dsl.module

val weatherModule =
    module {

        factory<WeatherRepository> {
            OpenWeatherMapRepository(
                client = get(),
                mapper = get(),
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
            )
        }

        single { WeatherMapper }
    }
