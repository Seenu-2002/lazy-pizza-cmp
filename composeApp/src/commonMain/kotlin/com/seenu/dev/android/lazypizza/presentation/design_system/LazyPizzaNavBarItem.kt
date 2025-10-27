package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key.Companion.L
import androidx.compose.ui.input.key.Key.Companion.M
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.primary8
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title4
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun LazyPizzaNavBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: Painter,
    label: String,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconModifier: Modifier
        val tint: Color
        val labelColor: Color
        if (selected) {
            iconModifier =
                Modifier.background(color = MaterialTheme.colorScheme.primary8, shape = CircleShape)
            tint = MaterialTheme.colorScheme.primary
            labelColor = MaterialTheme.colorScheme.textPrimary
        } else {

            iconModifier = Modifier
            tint = MaterialTheme.colorScheme.textSecondary
            labelColor = MaterialTheme.colorScheme.textSecondary
        }
        Box(
            modifier = iconModifier
                .padding(8.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = tint
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, style = MaterialTheme.typography.title4, color = labelColor)
    }

}