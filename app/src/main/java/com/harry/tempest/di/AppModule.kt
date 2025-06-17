package com.harry.tempest.di

import com.harry.tempest.TempestViewModel
import com.harry.tempest.navigation.GetStartDestinationUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        viewModel {
            TempestViewModel(
                getStartDestinationUseCase = get(),
            )
        }

        factory {
            GetStartDestinationUseCase(locationRepository = get())
        }
    }
