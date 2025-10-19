package com.seenu.dev.android.lazypizza.presentation.utils

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

@Composable
fun isExpanded(): Boolean {
    return currentWindowAdaptiveInfo()
        .windowSizeClass
        .isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        )
}