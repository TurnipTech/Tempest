package com.harry.location.di

import com.harry.location.domain.usecase.SearchLocationsUseCase
import com.harry.location.domain.usecase.SetLocationUseCase
import com.harry.location.ui.SearchLocationViewModel
import com.harry.location.ui.mapper.AndroidResourceProvider
import com.harry.location.ui.mapper.ResourceProvider
import com.harry.location.ui.mapper.SearchLocationUiMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchLocationModule =
    module {
        single { SearchLocationUiMapper() }

        single<ResourceProvider> { AndroidResourceProvider(context = get()) }

        factory { SearchLocationsUseCase(locationRepository = get()) }

        factory { SetLocationUseCase(locationRepository = get()) }

        viewModel {
            SearchLocationViewModel(
                searchLocationsUseCase = get(),
                setLocationUseCase = get(),
                searchLocationUiMapper = get(),
                resourceProvider = get(),
            )
        }
    }
