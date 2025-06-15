package com.harry.location.di

import com.harry.location.data.mapper.LocationMapper
import com.harry.location.data.repository.OpenWeatherMapLocationRepository
import com.harry.location.domain.repository.LocationRepository
import org.koin.dsl.module

fun locationModule(apiKey: String) =
    module {
        factory<LocationRepository> {
            OpenWeatherMapLocationRepository(
                client = get(),
                mapper = get(),
                apiKey = apiKey,
            )
        }

        single { LocationMapper }
    }
