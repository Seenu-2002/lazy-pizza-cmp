package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.input.key.Key.Companion.P
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.primaryGradientEnd
import com.seenu.dev.android.lazypizza.presentation.theme.primaryGradientStart

@Composable
fun LazyPizzaTextButton(
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .semantics {
                role = Role.Button
            }
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight,
            )
            .let {
                if (enabled) {
                    it.dropShadow(
                            shape = CircleShape,
                            shadow = androidx.compose.ui.graphics.shadow.Shadow(
                                radius = 6.dp,
                                spread = 0.dp,
                                color = Color(0x40F36B50),
                                offset = DpOffset(x = 0.dp, 4.dp)
                            )
                        )
                        .background(
                            shape = CircleShape,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryGradientEnd,
                                    MaterialTheme.colorScheme.primaryGradientStart,
                                ),
                            )
                        )
                } else {
                    it.background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                }
            }
            .clip(CircleShape)
            .clickable(enabled = enabled && !isLoading, onClick = onClick)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
        content = content
    )

}