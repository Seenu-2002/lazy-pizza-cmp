package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.primary8
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.title2
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
private fun ToppingCardPreviewWithCounter() {
    LazyPizzaTheme {
        ToppingCard(
            data = ToppingUiModel(
                id = 1L,
                name = "Extra Cheese",
                price = 1.5,
                imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/chilli_wm4ain.png",
                countInCart = 3
            ),
            onClick = {},
            onAdd = {},
            onRemove = {}
        )
    }
}

@Preview
@Composable
private fun ToppingCardPreview() {
    var data by remember {
        mutableStateOf(
            ToppingUiModel(
                id = 1L,
                name = "Extra Cheese",
                price = 1.5,
                imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/chilli_wm4ain.png",
                countInCart = 0
            )
        )
    }
    LazyPizzaTheme {
        ToppingCard(
            data = data,
            onClick = {
                data = data.copy(countInCart = 1)
            },
            onAdd = {
                data = data.copy(countInCart = data.countInCart + 1)
            },
            onRemove = {
                data = data.copy(countInCart = (data.countInCart - 1).coerceAtLeast(0))
            }
        )
    }
}

@Composable
fun ToppingCard(
    modifier: Modifier = Modifier,
    data: ToppingUiModel,
    onClick: () -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val isInCart = data.countInCart > 0
    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .background(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceHigher
            )
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable(
                enabled = !isInCart,
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
                color = if (isInCart) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 12.dp)
                .size(56.dp)
                .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary8)
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = data.imageUrl,
                contentDescription = data.name,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = data.name,
            style = MaterialTheme.typography.body3Regular,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isInCart) {
            ItemCounter(
                modifier = Modifier
                    .fillMaxWidth(),
                count = data.countInCart,
                name = data.name,
                max = 3,
                onAdd = onAdd,
                onRemove = onRemove
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val currencyFormatter = LocalCurrencyFormatter.current
                Text(
                    text = currencyFormatter.format(data.price),
                    style = MaterialTheme.typography.title2
                )
            }
        }
    }
}
