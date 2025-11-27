package com.seenu.dev.android.lazypizza

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.seenu.dev.android.lazypizza.di.initKoin
import com.seenu.dev.android.lazypizza.di.lazyPizzaModule
import com.seenu.dev.android.lazypizza.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class LazyPizzaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LazyPizzaApplication)
            androidLogger(Level.DEBUG)
            modules(
                lazyPizzaModule,
                platformModule
            )
        }

    }

}