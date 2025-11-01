package com.seenu.dev.android.lazypizza.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.seenu.dev.android.lazypizza.database.DatabaseDriverFactory
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

//@Module
//@ComponentScan
//class PlatformModule {
//
//    @Single
//    fun providesLazyPizzaDatabaseDriver(context: Context): SqlDriver {
//        return DatabaseDriverFactory(
//            context
//        ).create()
//    }
//
//}

val platformModule = org.koin.dsl.module {
    single<SqlDriver> { DatabaseDriverFactory(get()).create() }
}