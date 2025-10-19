package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.outline50
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title2
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.ic_minus
import lazypizza.composeapp.generated.resources.ic_plus
import lazypizza.composeapp.generated.resources.instrument_sans_bold
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun ItemCounterPreview() {
    LazyPizzaTheme {
        ItemCounter(
            count = 2,
            name = "Extra Cheese",
            onAdd = {},
            max = 3,
            onRemove = {}
        )
    }
}

@Composable
fun ItemCounter(
    modifier: Modifier = Modifier,
    count: Int,
    name: String,
    max: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LazyPizzaIconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(22.dp)
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Res.drawable.ic_minus),
                contentDescription = "Remove $name to cart",
                tint = LocalContentColor.current
            )
        }
        Text(text = "$count", style = MaterialTheme.typography.title2)
        LazyPizzaIconButton(
            onClick = onAdd,
            modifier = Modifier
                .size(22.dp),
            enabled = count < max
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Res.drawable.ic_plus),
                contentDescription = "Add $name to cart",
                tint = LocalContentColor.current
            )
        }
    }
}