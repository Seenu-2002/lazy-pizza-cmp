package com.seenu.dev.android.lazypizza

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.seenu.dev.android.lazypizza.core.formatter.CurrencyFormatter

val LocalCurrencyFormatter = staticCompositionLocalOf<CurrencyFormatter> {
    error("No CurrencyFormatter provided")
}

@Composable
fun LazyPizzaCompositionLocals(content: @Composable () -> Unit) {
    val currencyFormatter = CurrencyFormatter()

    CompositionLocalProvider(
        LocalCurrencyFormatter provides currencyFormatter,
        content = content
    )
}