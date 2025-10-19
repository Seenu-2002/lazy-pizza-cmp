package com.seenu.dev.android.lazypizza

import android.app.Application
import com.seenu.dev.android.lazypizza.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class LazyPizzaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LazyPizzaApplication)
            androidLogger(Level.DEBUG)
        }
    }

}