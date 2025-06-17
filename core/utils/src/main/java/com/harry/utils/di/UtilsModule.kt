package com.harry.utils.di

import com.harry.utils.AndroidResourceProvider
import com.harry.utils.ResourceProvider
import org.koin.dsl.module

val utilsModule =
    module {
        single<ResourceProvider> { AndroidResourceProvider(context = get()) }
    }
