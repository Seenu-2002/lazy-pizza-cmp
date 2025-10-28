package com.seenu.dev.android.lazypizza.presentation.pizza_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger.Companion.i
import coil3.compose.SubcomposeAsyncImage
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.design_system.ToppingCard
import com.seenu.dev.android.lazypizza.presentation.mappers.toUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary8
import com.seenu.dev.android.lazypizza.presentation.theme.title1SemiBold
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.add_extra_toppings
import lazypizza.composeapp.generated.resources.add_to_cart_total
import lazypizza.composeapp.generated.resources.ic_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaDetailScreen(id: String, onBack: () -> Unit, openCartScreen: () -> Unit) {
    val viewModel: PizzaDetailViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (uiState is UiState.Empty) {
            viewModel.handleEvent(PizzaDetailIntent.LoadPizzaDetail(id))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                is PizzaDetailSideEffect.OpenCart -> {
                    openCartScreen()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {

            }, navigationIcon = {
                Box(
                    modifier = Modifier.size(44.dp)
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.textSecondary8,
                            shape = CircleShape
                        ).clickable(
                            onClick = onBack,
                            role = Role.Button,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                        ).semantics {
                            role = Role.Button
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = "Back to Pizza list screen",
                        tint = MaterialTheme.colorScheme.textSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Empty, is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    PizzaDetailContent(
                        data = state.data.pizza,
                        toppings = state.data.toppings,
                        total = state.data.cartTotal,
                        onAddTopping = {
                            viewModel.handleEvent(PizzaDetailIntent.AddTopping(it))
                        },
                        onRemoveTopping = {
                            viewModel.handleEvent(PizzaDetailIntent.RemoveTopping(it))
                        },
                        onAddToCart = {
                            viewModel.handleEvent(PizzaDetailIntent.UpdateCart)
                        }
                    )
                }

                is UiState.Error -> {
                    i { "Error state: ${state.message}" }
                    Text(text = state.message)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PizzaDetailContentPreview() {
    LazyPizzaTheme {
        val item = FoodItem(
            id = "2",
            name = "Pepperoni",
            type = FoodType.PIZZA,
            ingredients = listOf("Tomato sauce", "mozzarella", "pepperoni"),
            price = 9.99,
            imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Pepperoni_ety6cd.png"
        )
        val toppings = (0..20).map {
            ToppingUiModel(
                id = it.toString(),
                name = "Bacon",
                price = 1.0,
                imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/bacon_jsutui.png",
                countInCart = it % 4
            )
        }
        PizzaDetailContent(
            data = item.toUiModel(),
            toppings = toppings,
            total = 12.99,
            onAddTopping = {},
            onRemoveTopping = {},
            onAddToCart = {})
    }
}

@Composable
fun PizzaDetailContent(
    modifier: Modifier = Modifier,
    data: FoodItemUiModel,
    toppings: List<ToppingUiModel>,
    total: Double,
    onAddTopping: (ToppingUiModel) -> Unit,
    onRemoveTopping: (ToppingUiModel) -> Unit,
    onAddToCart: () -> Unit
) {
    PizzaDetailContainer(
        modifier = modifier,
        pizzaImage = {
            SubcomposeAsyncImage(
                model = data.imageUrl,
                contentDescription = data.name,
                modifier = Modifier
                    .size(240.dp)
                    .aspectRatio(1F),
                loading = {
                    Text(text = "Loading...") // FIXME: Move it to shimmer
                }
            )
        },
        pizzaInfo = {
            Text(
                text = data.name,
                style = MaterialTheme.typography.title1SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            data.ingredientsFormatted?.let { ingredients ->
                Text(
                    text = ingredients,
                    style = MaterialTheme.typography.body3Regular,
                    color = MaterialTheme.colorScheme.textSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        toppingsContent = {
            ToppingsList(
                toppings = toppings,
                modifier = Modifier.weight(1F),
                onAddTopping = onAddTopping,
                onRemoveTopping = onRemoveTopping
            )
        },
        addToCartButton = {
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
                    .dropShadow(
                        shape = CircleShape,
                        shadow = Shadow(
                            radius = 10.dp,
                            spread = 6.dp,
                            color = Color(0x40F36B50),
                            offset = DpOffset(x = 0.dp, y = 4.dp)
                        )
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFF36B50), Color(0xFFF9966F))
                        ),
                        shape = CircleShape
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                val currencyFormatter = LocalCurrencyFormatter.current
                Text(
                    text = stringResource(
                        Res.string.add_to_cart_total,
                        currencyFormatter.format(total)
                    )
                )
            }
        }

    )

}

@Preview
@Composable
fun ToppingsListPreview() {
    LazyPizzaTheme {
        val toppings = (0..20).map {
            ToppingUiModel(
                id = "$it",
                name = "Bacon",
                price = 1.0,
                imageUrl = "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/bacon_jsutui.png",
                countInCart = it % 4
            )
        }
        ToppingsList(toppings = toppings, onAddTopping = {}, onRemoveTopping = {})
    }
}

@Composable
fun ToppingsList(
    toppings: List<ToppingUiModel>,
    modifier: Modifier = Modifier,
    onAddTopping: (ToppingUiModel) -> Unit = {},
    onRemoveTopping: (ToppingUiModel) -> Unit = {}
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(toppings) {
            ToppingCard(
                data = it,
                onClick = {
                    onAddTopping(it)
                },
                onAdd = {
                    onAddTopping(it)
                },
                onRemove = {
                    onRemoveTopping(it)
                },
            )
        }
    }
}

@Composable
fun PizzaDetailContainer(
    modifier: Modifier = Modifier,
    pizzaImage: @Composable () -> Unit,
    pizzaInfo: @Composable () -> Unit,
    toppingsContent: @Composable ColumnScope.() -> Unit,
    addToCartButton: @Composable () -> Unit
) {
    if (isExpanded()) {

        Row(modifier = modifier) {
            Column(modifier = Modifier.weight(1F).padding(16.dp)) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    pizzaImage()
                }
                pizzaInfo()
            }
            Column(
                modifier = Modifier.weight(1F)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceHigher, shape = RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.add_extra_toppings),
                    style = MaterialTheme.typography.label2Semibold,
                    color = MaterialTheme.colorScheme.textSecondary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                toppingsContent()
                addToCartButton()
            }
        }

    } else {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    pizzaImage()
                }

                val radius = 24.dp
                val shape = RoundedCornerShape(topStart = radius)
                val color = MaterialTheme.colorScheme.surfaceHigher
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .drawBehind {
                            val radiusInPx = with(density) { radius.toPx() }
                            val path = Path()
                            path.moveTo(0F, radiusInPx)
                            path.cubicTo(
                                0F, radiusInPx,
                                0F, 0F,
                                radiusInPx, 0F
                            )
                            path.lineTo(size.width - radiusInPx, 0F)
                            path.cubicTo(
                                size.width - radiusInPx, 0F,
                                size.width, 0F,
                                size.width, -radiusInPx
                            )
                            path.lineTo(size.width, size.height)
                            path.lineTo(0F, size.height)
                            path.lineTo(0F, radiusInPx)
                            drawPath(path, color = color)
                        }
                        .dropShadow(
                            shape = shape,
                            shadow = Shadow(
                                radius = 16.dp,
                                spread = 0.dp,
                                color = Color(0x03131F0A),
                                offset = DpOffset(x = 4.dp, 4.dp)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    pizzaInfo()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(Res.string.add_extra_toppings),
                        style = MaterialTheme.typography.label2Semibold,
                        color = MaterialTheme.colorScheme.textSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    toppingsContent()
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color.White,
                                Color.White,
                            )
                        )
                    )
                    .padding(
                        top = 36.dp,
                        bottom = 8.dp
                    )
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
            ) {
                addToCartButton()
            }
        }
    }
}