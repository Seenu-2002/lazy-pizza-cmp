package com.seenu.dev.android.lazypizza

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.seenu.dev.android.lazypizza.core.formatter.CurrencyFormatter
import com.seenu.dev.android.lazypizza.presentation.design_system.Dimensions
import com.seenu.dev.android.lazypizza.presentation.design_system.mobileDimensions
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import com.seenu.dev.android.lazypizza.presentation.design_system.tabletDimensions
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded

val LocalCurrencyFormatter = staticCompositionLocalOf<CurrencyFormatter> {
    error("No CurrencyFormatter provided")
}
val LocalDimensions = staticCompositionLocalOf<Dimensions> {
    error("No Dimensions provided")
}

@Composable
fun LazyPizzaCompositionLocals(content: @Composable () -> Unit) {
    val currencyFormatter = CurrencyFormatter()

    val dimensions = if (isExpanded()) {
        tabletDimensions
    } else {
        mobileDimensions
    }

    CompositionLocalProvider(
        LocalCurrencyFormatter provides currencyFormatter,
        LocalDimensions provides dimensions,
        content = content
    )
}