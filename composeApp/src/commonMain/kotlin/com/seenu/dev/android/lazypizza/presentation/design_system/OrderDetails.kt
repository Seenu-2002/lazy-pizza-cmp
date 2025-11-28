package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter.State.Empty.painter
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.InstrumentSans
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.chevron_down
import lazypizza.composeapp.generated.resources.comments
import lazypizza.composeapp.generated.resources.ic_back
import lazypizza.composeapp.generated.resources.order_details
import lazypizza.composeapp.generated.resources.recommended_items
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ColumnScope.OrderDetails(
    isExpanded: Boolean,
    cartItems: List<CartItemUiModel>,
    recommendedItems: List<FoodItemUiModel>,
    onExpandClick: () -> Unit,
    onAddItemToCart: (String) -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTablet = isExpanded()
    val gridCount = if (isTablet) 2 else 1

    Row(
        modifier = modifier.fillMaxWidth()
            .clickable(onClick = onExpandClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CheckoutHeaderText(
            text = stringResource(Res.string.order_details).uppercase(),
            modifier = Modifier.fillMaxWidth()
        )

        LazyPizzaCounterIconButton(
            onClick = onExpandClick,
            modifier = Modifier.padding(4.dp)
        ) {
            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 180F else 0F,
                label = "ChevronRotation"
            )
            Icon(
                painter = painterResource(Res.drawable.chevron_down),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = rotation
                    },
                tint = MaterialTheme.colorScheme.textPrimary
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    AnimatedVisibility(visible = isExpanded) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val count = cartItems.size.let {
                if (it % gridCount == 0) it / gridCount else (it / gridCount) + 1
            }
            for (count in 0 until count) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 0 until gridCount) {
                        val index = (count * gridCount) + i
                        if (index < cartItems.size) {
                            val cartItem = cartItems[index]
                            CartItemCard(
                                modifier = Modifier.weight(1F)
                                    .fillMaxHeight(),
                                item = cartItem,
                                onIncreaseQuantity = {
                                    onIncreaseQuantity(cartItem.foodItem.id)
                                },
                                onDecreaseQuantity = {
                                    onDecreaseQuantity(cartItem.foodItem.id)
                                },
                                onRemove = {
                                    onRemoveItem(cartItem.foodItem.id)
                                }
                            )

                        } else {
                            Spacer(modifier = Modifier.weight(1F))
                        }
                    }
                }
            }
        }
    }
    AnimatedVisibility(visible = isExpanded) {
        Spacer(modifier = Modifier.height(12.dp))
    }
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline
    )
    Spacer(modifier = Modifier.height(12.dp))
    CheckoutHeaderText(
        text = stringResource(Res.string.recommended_items).uppercase(),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recommendedItems, key = {
            it.id
        }) {
            AddOnCard(modifier = Modifier.animateItem(), data = it, onClick = {
                onAddItemToCart(it.id)
            })
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline
    )
}