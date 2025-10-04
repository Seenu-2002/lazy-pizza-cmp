package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodType
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
                    id = 1L,
                    name = "Veggie Delight",
                    type = FoodType.PIZZA,
                    ingredients = "Tomato, Lettuce, Olives, Bell Peppers",
                    prize = 9.99,
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
            id = 1L,
            name = "Veggie Delight",
            type = FoodType.PIZZA,
            ingredients = "Tomato, Lettuce, Olives, Bell Peppers",
            prize = 9.99,
        )
        FoodPreviewItemCard(data = data)
    }
}

@Preview
@Composable
fun FoodPreviewItemCardWithCardPreview() {
    LazyPizzaTheme {
        val data = FoodItemUiModel(
            id = 1L,
            name = "Mineral Water",
            type = FoodType.DRINK,
            ingredients = null,
            prize = 9.99,
        )
        FoodPreviewItemCard(data = data, showAddToCard = true)
    }
}

@Composable
fun FoodPreviewItemCard(
    data: FoodItemUiModel,
    modifier: Modifier = Modifier,
    showAddToCard: Boolean = false,
    onClick: () -> Unit = {},
    onAdd: () -> Unit = {},
    onRemove: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val isItemAddedToCard = data.countInCart > 0
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier.fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                shape = shape,
                color = MaterialTheme.colorScheme.surfaceHigher
            ).clickable(
                enabled = !isItemAddedToCard,
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
            Box(modifier = Modifier.size(108.dp).padding(4.dp))
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
                    LazyPizzaIconButton(
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
            data.ingredients?.let { ingredients ->
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
                    val prizeLabel = "$${data.prize}" // TODO: Currency formatting
                    Text(
                        text = prizeLabel,
                        style = MaterialTheme.typography.title1SemiBold,
                        modifier = Modifier.weight(1F)
                    )

                    if (showAddToCard) {
                        TextButton(
                            onClick = {

                            }, modifier = Modifier.border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary8,
                                shape = CircleShape
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(
                                horizontal = 24.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.add_to_cart),
                                style = MaterialTheme.typography.title3
                            )
                        }
                    }
                } else {
                    ItemCounter(
                        modifier = Modifier.width(100.dp),
                        count = data.countInCart,
                        name = data.name,
                        onAdd = onAdd,
                        onRemove = onRemove
                    )

                    Spacer(modifier = Modifier.weight(1F))
                    val total = "$${data.countInCart * data.prize}" // TODO: Currency formatting
                    Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                        Text(
                            text = total,
                            style = MaterialTheme.typography.title1SemiBold
                        )
                        Text(
                            text = stringResource(
                                Res.string.count_and_price,
                                data.countInCart,
                                data.prize
                            ),
                            style = MaterialTheme.typography.body4Regular
                        )
                    }
                }
            }
        }
    }
}