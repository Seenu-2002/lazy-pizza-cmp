package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.state.ToppingItemUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.outline50
import com.seenu.dev.android.lazypizza.presentation.theme.primary8
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
private fun ToppingItemCardPreviewWithCounter() {
    LazyPizzaTheme {
        ToppingItemCard(
            data = ToppingItemUiModel(
                id = 1L,
                name = "Extra Cheese",
                prize = 1.5,
                prizeLabel = "$1.5"
            ),
            count = 2,
            onClick = {},
            onAdd = {},
            onRemove = {}
        )
    }
}

@Preview
@Composable
private fun ToppingItemCardPreview() {
    var count by remember {
        mutableIntStateOf(0)
    }
    LazyPizzaTheme {
        ToppingItemCard(
            data = ToppingItemUiModel(
                id = 1L,
                name = "Extra Cheese",
                prize = 1.5,
                prizeLabel = "$1.5"
            ),
            count = count,
            onClick = {
                count = 1
            },
            onAdd = {
                count++
            },
            onRemove = {
                count--
            }
        )
    }
}

@Composable
fun ToppingItemCard(
    modifier: Modifier = Modifier,
    data: ToppingItemUiModel,
    count: Int,
    onClick: () -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            )
            .clip(shape = MaterialTheme.shapes.medium)

            .clickable(
                enabled = count == 0,
                onClick = onClick,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = true,
                    color = Color.Unspecified
                )
            )
            .border(
                1.dp,
                color = if (count == 0) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier.size(64.dp)
                    .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary8)
            )
        }

        Text(
            text = data.name,
            style = MaterialTheme.typography.body3Regular,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (count > 0) {
            ItemCounter(
                modifier = Modifier
                    .fillMaxWidth(),
                count = count,
                name = data.name,
                onAdd = onAdd,
                onRemove = onRemove
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.prizeLabel,
                    style = MaterialTheme.typography.title2
                )
            }
        }
    }
}
