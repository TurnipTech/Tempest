package com.harry.tempest.di

import com.harry.tempest.TempestViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        viewModel {
            TempestViewModel(
                getStartDestinationUseCase = get(),
            )
        }
    }
