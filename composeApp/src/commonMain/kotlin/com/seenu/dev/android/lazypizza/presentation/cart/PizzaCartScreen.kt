package com.seenu.dev.android.lazypizza.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.lazypizza.presentation.design_system.CartItemCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaInfoCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaTextButton
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title2
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.back_to_menu
import lazypizza.composeapp.generated.resources.cart
import lazypizza.composeapp.generated.resources.empty_cart
import lazypizza.composeapp.generated.resources.empty_cart_msg
import lazypizza.composeapp.generated.resources.proceed_to_checkout
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun PizzaCartScreenPreview() {
    LazyPizzaTheme { PizzaCartScreen({}) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaCartScreen(onBackToMenu: () -> Unit) {

    val viewModel: PizzaCartViewModel = koinViewModel()
    val cartState by viewModel.cart.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (cartState is UiState.Empty) {
            viewModel.onIntent(CartIntent.GetCartItems)
        }

        viewModel.events.collect {
            when (it) {
                CartSideEffect.Checkout -> {
                    // TODO: Checkout logic
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(Res.string.cart),
                    style = MaterialTheme.typography.body1Medium,
                    color = MaterialTheme.colorScheme.textPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        when (cartState) {
            is UiState.Empty, is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val cart = (cartState as UiState.Success).data
                if (cart.items.isNotEmpty()) {
                    CartItemList(
                        modifier = Modifier.fillMaxWidth(),
                        items = cart.items,
                        cartTotal = cart.total,
                        onIncreaseQuantity = { foodItem ->
                            val intent = CartIntent.UpdateItemQuantity(
                                itemId = foodItem.foodItem.id,
                                quantity = foodItem.foodItem.countInCart + 1
                            )
                            viewModel.onIntent(intent)
                        },
                        onDecreaseQuantity = { foodItem ->
                            val intent = CartIntent.UpdateItemQuantity(
                                itemId = foodItem.foodItem.id,
                                quantity = (foodItem.foodItem.countInCart - 1).coerceAtLeast(1)
                            )
                            viewModel.onIntent(intent)
                        },
                        onRemove = { foodItem ->
                            val intent = CartIntent.DeleteItem(itemId = foodItem.foodItem.id)
                            viewModel.onIntent(intent)
                        })
                } else {
                    Spacer(modifier = Modifier.height(120.dp))
                    EmptyCartCard(onBackToMenu = onBackToMenu, modifier = Modifier.fillMaxWidth())
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (cartState as UiState.Error).message,
                        style = MaterialTheme.typography.body3Regular,
                        color = MaterialTheme.colorScheme.textSecondary
                    )
                }
            }
        }
    }

}

@Composable
fun CartItemList(
    modifier: Modifier = Modifier,
    items: List<CartItemUiModel>,
    cartTotal: Double,
    onIncreaseQuantity: (CartItemUiModel) -> Unit = {},
    onDecreaseQuantity: (CartItemUiModel) -> Unit = {},
    onRemove: (CartItemUiModel) -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items, key = {
                it.foodItem.id
            }) { cartItem ->
                CartItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    item = cartItem,
                    onIncreaseQuantity = {
                        onIncreaseQuantity(cartItem)
                    },
                    onDecreaseQuantity = {
                        onDecreaseQuantity(cartItem)
                    },
                    onRemove = {
                        onRemove(cartItem)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 4.dp)
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.0F),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.75F),
                            )
                        )
                    )
            )
            LazyPizzaTextButton(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.proceed_to_checkout, cartTotal),
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun EmptyCartCard(onBackToMenu: () -> Unit, modifier: Modifier = Modifier) {
    LazyPizzaInfoCard(
        title = stringResource(Res.string.empty_cart),
        message = stringResource(Res.string.empty_cart_msg),
        actionLabel = stringResource(Res.string.back_to_menu),
        onActionClick = onBackToMenu,
        modifier = modifier
    )
}