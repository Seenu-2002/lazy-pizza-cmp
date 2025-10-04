package com.seenu.dev.android.lazypizza.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColorTheme = lightColorScheme(
    primary = Primary,
    background = Background,
    onPrimary = TextOnPrimary,
    outline = Outline,
    primaryContainer = Background
)

val ColorScheme.textPrimary
    get() = TextPrimary

val ColorScheme.textSecondary
    get() = TextSecondary

val ColorScheme.textSecondary8
    get() = TextSecondary8

val ColorScheme.surfaceHigher: Color
    get() = SurfaceHigher

val ColorScheme.surfaceHighest
    get() = SurfaceHighest

val ColorScheme.outline50
    get() = Outline50

val ColorScheme.primaryGradientStart
    get() = PrimaryGradientStart

val ColorScheme.primaryGradientEnd
    get() = PrimaryGradientEnd

val ColorScheme.primary8
    get() = Primary8

@Composable
fun LazyPizzaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorTheme,
        typography = LazyPizzaTypography,
        content = content
    )
}