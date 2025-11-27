package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.body4Regular
import com.seenu.dev.android.lazypizza.presentation.theme.primary8
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.title1SemiBold
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.add_to_cart
import lazypizza.composeapp.generated.resources.count_and_price
import lazypizza.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun FoodPreviewItemCardWithCountPreview() {
    LazyPizzaTheme {
        var data by remember {
            mutableStateOf(
                FoodItemUiModel(
                    id = "1",
                    name = "Veggie Delight",
                    type = FoodType.PIZZA,
                    ingredientsFormatted = "Tomato, Lettuce, Olives, Bell Peppers",
                    price = 9.99,
                    imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Pepperoni_ety6cd.png",
                    ingredients = emptyList(),
                    countInCart = 4
                )
            )
        }
        FoodPreviewItemCard(
            data = data,
            onClick = {
                data = data.copy(countInCart = 1)
            },
            onDelete = {
                data = data.copy(countInCart = 0)
            }, onAdd = {
                data = data.copy(countInCart = data.countInCart + 1)
            }, onRemove = {
                if (data.countInCart > 0) {
                    data = data.copy(countInCart = data.countInCart - 1)
                }
            })
    }
}

@Preview
@Composable
fun FoodPreviewItemCardPreview() {
    LazyPizzaTheme {
        val data = FoodItemUiModel(
            id = "1",
            name = "Veggie Delight",
            type = FoodType.PIZZA,
            ingredients = emptyList(),
            ingredientsFormatted = "Tomato, Lettuce, Olives, Bell Peppers",
            imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Pepperoni_ety6cd.png",
            price = 9.99,
        )
        FoodPreviewItemCard(data = data)
    }
}

@Preview
@Composable
fun FoodPreviewItemCardWithCardPreview() {
    LazyPizzaTheme {
        val data = FoodItemUiModel(
            id = "1",
            name = "Mineral Water",
            type = FoodType.DRINK,
            ingredients = emptyList(),
            ingredientsFormatted = null,
            imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Pepperoni_ety6cd.png",
            price = 9.99,
        )
        FoodPreviewItemCard(data = data, showAddToCart = true)
    }
}

@Composable
fun FoodPreviewItemCard(
    data: FoodItemUiModel,
    modifier: Modifier = Modifier,
    showAddToCart: Boolean = false,
    maxCountCanBeAdded: Int = 3,
    onClick: () -> Unit = {},
    onAdd: () -> Unit = {},
    onRemove: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val isItemAddedToCard = data.countInCart > 0
    val shape = MaterialTheme.shapes.medium
    val currencyFormatter = LocalCurrencyFormatter.current
    Row(
        modifier = modifier.fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(shape)
            .background(
                shape = shape,
                color = MaterialTheme.colorScheme.surfaceHigher
            ).clickable(
                enabled = !isItemAddedToCard && !showAddToCart,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(2.dp)
                .background(
                    shape = shape.copy(
                        topEnd = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    ),
                    color = MaterialTheme.colorScheme.surfaceHighest
                )
        ) {
            SubcomposeAsyncImage(
                model = data.imageUrl,
                contentDescription = data.name,
                modifier = Modifier
                    .size(108.dp)
                    .padding(4.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1F),
                    text = data.name,
                    style = MaterialTheme.typography.body1Medium
                )
                if (data.countInCart > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    LazyPizzaCounterIconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_trash),
                            contentDescription = "Remove ${data.name} from cart",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            data.ingredientsFormatted?.let { ingredients ->
                Spacer(modifier = Modifier.height(8.dp))
                if (!isItemAddedToCard) {
                    Text(
                        text = ingredients,
                        style = MaterialTheme.typography.body3Regular
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1F))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isItemAddedToCard) {
                    val prizeLabel = currencyFormatter.format(data.price)
                    Text(
                        text = prizeLabel,
                        style = MaterialTheme.typography.title1SemiBold,
                        modifier = Modifier.weight(1F)
                    )

                    if (showAddToCart) {
                        Box(
                            modifier = Modifier
                                .semantics {
                                    role = Role.Button
                                }
                                .clip(CircleShape)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary8,
                                    shape = CircleShape
                                )
                                .clickable(
                                    onClick = onAdd,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(
                                        bounded = true,
                                        color = MaterialTheme.colorScheme.primary8
                                    )
                                )
                                .padding(vertical = 9.dp, horizontal = 24.dp),
                        ) {
                            Text(
                                text = stringResource(Res.string.add_to_cart),
                                style = MaterialTheme.typography.title3,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    ItemCounter(
                        modifier = Modifier.width(100.dp),
                        count = data.countInCart,
                        name = data.name,
                        max = maxCountCanBeAdded,
                        onAdd = onAdd,
                        onRemove = onRemove
                    )

                    Spacer(modifier = Modifier.weight(1F))
                    val total = currencyFormatter.format(data.countInCart * data.price)
                    Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                        Text(
                            text = total,
                            style = MaterialTheme.typography.title1SemiBold
                        )
                        Text(
                            text = stringResource(
                                Res.string.count_and_price,
                                data.countInCart,
                                data.price
                            ),
                            style = MaterialTheme.typography.body4Regular
                        )
                    }
                }
            }
        }
    }
}