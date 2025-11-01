package com.seenu.dev.android.lazypizza.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.seenu.dev.android.lazypizza.LazyPizzaDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory constructor(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(LazyPizzaDatabase.Schema, context, "lazypizza.db")
    }
}