package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.body4Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1SemiBold
import com.seenu.dev.android.lazypizza.presentation.utils.roundTo2Digits
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.count_and_price
import lazypizza.composeapp.generated.resources.ic_trash
import lazypizza.composeapp.generated.resources.topping_cart_format
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun CartItemCardPreview() {
    LazyPizzaTheme {
        var item by remember {
            mutableStateOf(
                CartItemUiModel(
                    foodItem = FoodItemUiModel(
                        id = "1",
                        name = "Margherita",
                        price = 8.99,
                        imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Pepperoni_ety6cd.png",
                        type = FoodType.PIZZA,
                        ingredients = emptyList(),
                        ingredientsFormatted = "Cheese, Tomato Sauce",
                        countInCart = 1
                    ),
                    toppings = listOf(
                        ToppingUiModel(
                            id = "t1",
                            name = "Extra Cheese",
                            price = 1.5,
                            countInCart = 1,
                            imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/chilli_wm4ain.png"
                        ),
                        ToppingUiModel(
                            id = "t2",
                            name = "Mushrooms",
                            price = 1.0,
                            countInCart = 2,
                            imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595129/Onions_h3F7D1.png"
                        )
                    )
                )
            )
        }

        CartItemCard(
            modifier = Modifier.fillMaxWidth(),
            item = item,
            onIncreaseQuantity = {
                item = item.copy(
                    foodItem = item.foodItem.copy(
                        countInCart = item.foodItem.countInCart + 1
                    )
                )
            },
            onDecreaseQuantity = {
                if (item.foodItem.countInCart > 0) {
                    item = item.copy(
                        foodItem = item.foodItem.copy(
                            countInCart = item.foodItem.countInCart - 1
                        )
                    )
                }
            },
        )
    }
}

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    item: CartItemUiModel,
    onIncreaseQuantity: () -> Unit = {},
    onDecreaseQuantity: () -> Unit = {},
    onRemove: () -> Unit = {}
) {
    val foodItem = item.foodItem
    val shape = MaterialTheme.shapes.medium
    val currencyFormatter = LocalCurrencyFormatter.current

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    Row(
        modifier = modifier
            .dropShadow(
                shape = shape,
                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                    radius = 16.dp,
                    spread = 0.dp,
                    color = Color(0x0F03131F),
                    offset = DpOffset(x = 0.dp, (-4).dp)
                )
            )
            .clip(shape)
            .background(
                shape = shape,
                color = MaterialTheme.colorScheme.surfaceHigher
            )
            .onSizeChanged {
                size = it
            }
    ) {
        val height = with(LocalDensity.current) {
            val dp = size.height.toDp()
            if (dp < 108.dp) 108.dp else dp
        }
        Box(
            modifier = Modifier
                .height(height)
                .padding(2.dp)
                .background(
                    shape = shape.copy(
                        topEnd = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    ),
                    color = MaterialTheme.colorScheme.surfaceHighest
                ),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = foodItem.imageUrl,
                contentDescription = foodItem.name,
                modifier = Modifier
                    .width(108.dp)
                    .heightIn(min = 108.dp)
                    .padding(4.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1F)
                .heightIn(min = 108.dp)
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1F),
                    text = foodItem.name,
                    style = MaterialTheme.typography.body1Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                LazyPizzaCounterIconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(22.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_trash),
                        contentDescription = "Remove ${foodItem.name} from cart",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            for (topping in item.toppings) {
                Text(
                    text = stringResource(
                        Res.string.topping_cart_format,
                        topping.countInCart,
                        topping.name
                    ),
                    style = MaterialTheme.typography.body3Regular,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.textSecondary
                )
            }
            Spacer(modifier = Modifier.weight(1F))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemCounter(
                    modifier = Modifier.width(100.dp),
                    count = foodItem.countInCart,
                    name = foodItem.name,
                    min = 1,
                    max = 3,
                    onAdd = onIncreaseQuantity,
                    onRemove = onDecreaseQuantity
                )

                Spacer(modifier = Modifier.weight(1F))
                val total = currencyFormatter.format(item.itemTotalPrice)
                Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                    Text(
                        text = total,
                        style = MaterialTheme.typography.title1SemiBold
                    )
                    Text(
                        text = stringResource(
                            Res.string.count_and_price,
                            foodItem.countInCart,
                            item.singleItemPrice.roundTo2Digits()
                        ),
                        style = MaterialTheme.typography.body4Regular
                    )
                }
            }
        }
    }
}