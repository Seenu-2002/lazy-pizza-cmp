package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1SemiBold
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AddonCardWithCountPreview() {
    LazyPizzaTheme {
        var data by remember {
            mutableStateOf(
                FoodItemUiModel(
                    id = "1",
                    name = "Extra Cheese",
                    price = 1.5,
                    type = FoodType.SAUCE,
                    imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/chilli_wm4ain.png",
                    countInCart = 2,
                    ingredients = listOf(),
                    ingredientsFormatted = ""
                )
            )
        }
        AddOnCard(
            modifier = Modifier.padding(8.dp),
            data = data,
            onClick = {
                data = data.copy(countInCart = 1)
            },
            onAdd = {
                data = data.copy(countInCart = data.countInCart + 1)
            },
            onRemove = {
                if (data.countInCart > 0) {
                    data = data.copy(countInCart = data.countInCart - 1)
                }
            }
        )
    }
}

@Preview
@Composable
fun AddonCardPreview() {
    LazyPizzaTheme {
        AddOnCard(
            modifier = Modifier.padding(8.dp),
            data = FoodItemUiModel(
                id = "1",
                name = "Extra Cheese",
                price = 1.5,
                type = FoodType.SAUCE,
                imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/chilli_wm4ain.png",
                countInCart = 0,
                ingredients = listOf(),
                ingredientsFormatted = ""
            )
        )
    }
}

@Composable
fun AddOnCard(
    modifier: Modifier = Modifier,
    data: FoodItemUiModel,
    maxItemCanBeAdded: Int = 3,
    onClick: () -> Unit = {},
    onAdd: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    val isAddedInCart = data.countInCart > 0
    val shape = MaterialTheme.shapes.medium
    Column(
        modifier = modifier.width(IntrinsicSize.Max)
            .dropShadow(
                shape = shape,
                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                    radius = 16.dp,
                    spread = 0.dp,
                    color = Color(0x0F03131F),
                    offset = DpOffset(x = 0.dp, (4).dp)
                )
            )
            .clip(shape)
            .background(color = MaterialTheme.colorScheme.surfaceHigher, shape = shape)
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .background(
                    shape = shape.copy(
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    ),
                    color = MaterialTheme.colorScheme.surfaceHighest
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = data.imageUrl,
                contentDescription = data.name,
                modifier = Modifier
                    .aspectRatio(1F)
                    .sizeIn(minWidth = 108.dp, minHeight = 108.dp)
                    .padding(6.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = data.name,
                style = MaterialTheme.typography.body1Regular,
                color = MaterialTheme.colorScheme.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isAddedInCart) {
                ItemCounter(
                    modifier = Modifier.fillMaxWidth(),
                    count = data.countInCart,
                    name = data.name,
                    max = maxItemCanBeAdded,
                    onAdd = onAdd,
                    onRemove = onRemove
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val currentFormatter = LocalCurrencyFormatter.current
                    val priceLabel = currentFormatter.format(data.price)
                    Text(
                        text = priceLabel,
                        style = MaterialTheme.typography.title1SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    LazyPizzaIconButton(
                        onClick = onClick,
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_plus),
                            contentDescription = "Add ${data.name} to cart",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}