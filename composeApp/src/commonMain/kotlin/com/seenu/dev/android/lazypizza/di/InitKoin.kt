package com.seenu.dev.android.lazypizza.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

fun initKoin(configuration: KoinApplication.() -> Unit) {
    startKoin {
        configuration(this)
        modules(LazyPizzaModule().module)
    }
}