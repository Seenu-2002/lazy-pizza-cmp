package com.seenu.dev.android.lazypizza.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val LightColorTheme = lightColorScheme(
    primary = Primary,
    background = background,
    onPrimary = TextOnPrimary,
    outline = Outline,
)

val MaterialTheme.textPrimary
    get() = TextPrimary

val MaterialTheme.textSecondary
    get() = TextSecondary

val MaterialTheme.textSecondary8
    get() = TextSecondary8

val MaterialTheme.surfaceHighest
    get() = SurfaceHighest

val MaterialTheme.outline50
    get() = Outline50

val MaterialTheme.primaryGradientStart
    get() = PrimaryGradientStart

val MaterialTheme.primaryGradientEnd
    get() = PrimaryGradientEnd

val MaterialTheme.primary8
    get() = Primary8

@Composable
fun LazyPizzaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorTheme,
        typography = LazyPizzaTypography,
        content = content
    )
}