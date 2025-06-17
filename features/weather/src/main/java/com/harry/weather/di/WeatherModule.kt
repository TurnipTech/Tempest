package com.harry.weather.di

import com.harry.location.domain.model.Location
import com.harry.weather.data.WeatherRepository
import com.harry.weather.data.WeatherRepositoryImpl
import com.harry.weather.data.mapper.WeatherMapper
import com.harry.weather.domain.usecase.GetCurrentWeatherUseCase
import com.harry.weather.ui.WeatherViewModel
import com.harry.weather.ui.mapper.WeatherUiMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun weatherModule(apiKey: String) =
    module {
        factory<WeatherRepository> {
            WeatherRepositoryImpl(
                client = get(),
                mapper = get(),
                apiKey = apiKey,
            )
        }

        single { WeatherMapper }

        single { WeatherUiMapper(resourceProvider = get()) }

        factory { GetCurrentWeatherUseCase(repository = get()) }

        viewModel { (location: Location?) ->
            WeatherViewModel(
                location = location,
                getCurrentWeatherUseCase = get(),
                weatherUiMapper = get(),
                getStoredLocationUseCase = get(),
            )
        }
    }
