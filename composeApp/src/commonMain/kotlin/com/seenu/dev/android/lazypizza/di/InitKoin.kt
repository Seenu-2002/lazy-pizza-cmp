package com.seenu.dev.android.lazypizza.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(configuration: KoinApplication.() -> Unit) {
    startKoin {
        configuration(this)
        modules(
            lazyPizzaModule
        )
    }
}