package com.seenu.dev.android.lazypizza.database

import app.cash.sqldelight.db.SqlDriver
import com.seenu.dev.android.lazypizza.LazyPizzaDatabase
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            LazyPizzaDatabase.Schema,
            "lazypizza.db"
        )
    }
}