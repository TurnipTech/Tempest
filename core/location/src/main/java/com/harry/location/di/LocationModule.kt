package com.harry.location.di

import com.harry.location.data.mapper.LocationMapper
import com.harry.location.data.repository.LocationRepositoryImpl
import com.harry.location.domain.repository.LocationRepository
import com.harry.location.domain.usecase.GetStoredLocationUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

fun locationModule(apiKey: String) =
    module {
        factory<LocationRepository> {
            LocationRepositoryImpl(
                client = get(),
                mapper = get(),
                apiKey = apiKey,
                storage = get(),
                ioDispatcher = Dispatchers.IO,
            )
        }

        single { LocationMapper }

        factory { GetStoredLocationUseCase(locationRepository = get()) }
    }
