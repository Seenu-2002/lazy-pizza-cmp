package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.outline50
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary

@Composable
fun LazyPizzaCounterIconButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    content: @Composable () -> Unit = {}
) {
    val colors = IconButtonDefaults.iconButtonColors(
        disabledContentColor = MaterialTheme.colorScheme.textPrimary.copy(0.38F)
    )
    Box(
        modifier = modifier
            .clip(shape)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.outline50,
                shape = shape,
            )
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false,
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentColor = if (enabled) {
            colors.contentColor
        } else colors.disabledContentColor

        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}