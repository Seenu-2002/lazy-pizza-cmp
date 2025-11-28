package com.seenu.dev.android.lazypizza.presentation.checkout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger.Companion.e
import com.seenu.dev.android.lazypizza.presentation.design_system.CheckoutHeaderText
import com.seenu.dev.android.lazypizza.presentation.design_system.OrderConfirmation
import com.seenu.dev.android.lazypizza.presentation.design_system.OrderDetails
import com.seenu.dev.android.lazypizza.presentation.design_system.OrderTotal
import com.seenu.dev.android.lazypizza.presentation.design_system.SelectDateDialog
import com.seenu.dev.android.lazypizza.presentation.design_system.SelectTimeDialog
import com.seenu.dev.android.lazypizza.presentation.design_system.TimePickerOption
import com.seenu.dev.android.lazypizza.presentation.theme.InstrumentSans
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body2Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary8
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.add_comment
import lazypizza.composeapp.generated.resources.comments
import lazypizza.composeapp.generated.resources.earliest_available
import lazypizza.composeapp.generated.resources.ic_back
import lazypizza.composeapp.generated.resources.order_checkout
import lazypizza.composeapp.generated.resources.pickup_time
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCheckoutScreen(onNavigateBack: () -> Unit, goToMenu: () -> Unit) {

    val viewModel: OrderCheckoutViewModel = koinViewModel()
    val checkoutState by viewModel.checkoutUiState.collectAsStateWithLifecycle()

    val shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .dropShadow(
                shape = shape, shadow = Shadow(
                    radius = 16.dp,
                    spread = 0.dp,
                    color = Color(0x0A03131F),
                    offset = DpOffset(x = 0.dp, (-4).dp)
                )
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceHigher,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .navigationBarsPadding()
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(Res.string.order_checkout),
                    style = MaterialTheme.typography.body1Medium
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.textSecondary8
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.textSecondary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceHigher
            )
        )

        if (checkoutState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1F),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            return
        }

        val confirmation = checkoutState.confirmation
        if (confirmation != null) {
            Spacer(modifier = Modifier.height(120.dp))
            OrderConfirmation(
                modifier = Modifier
                    .widthIn(max = 450.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                data = confirmation,
                goToMenu = goToMenu
            )
            return
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1F)
                .padding(horizontal = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {

                val verticalScroll = rememberScrollState()
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .verticalScroll(verticalScroll)
                ) {
                    CheckoutHeaderText(
                        text = stringResource(Res.string.pickup_time).uppercase(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePickerOption(
                        selected = checkoutState.pickupTime,
                        onEarliestTimeSelected = {
                            viewModel.onIntent(OrderCheckoutIntent.SetPickupTime(PickupTime.EarliestAvailableTime))
                        },
                        onScheduleTimeSelected = {
                            viewModel.onIntent(
                                OrderCheckoutIntent.SetPickupTime(
                                    PickupTime.ScheduledTime(
                                        null,
                                        null
                                    )
                                )
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CheckoutHeaderText(
                            text = stringResource(Res.string.earliest_available).uppercase(),
                            modifier = Modifier.wrapContentSize()
                        )
                        Text(
                            text = checkoutState.earliestPickupTimeLabel,
                            fontFamily = InstrumentSans,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OrderDetails(
                        isExpanded = checkoutState.isOrderDetailsExpanded,
                        cartItems = checkoutState.cartItems,
                        recommendedItems = checkoutState.recommendedItems,
                        onExpandClick = {
                            viewModel.onIntent(
                                OrderCheckoutIntent.OnExpandOrderDetails(
                                    isExpanded = !checkoutState.isOrderDetailsExpanded
                                )
                            )
                        },
                        onAddItemToCart = { foodItemId ->
                            viewModel.onIntent(
                                OrderCheckoutIntent.AddItemToCart(
                                    foodItemId = foodItemId
                                )
                            )
                        },
                        onIncreaseQuantity = { foodItemId ->
                            viewModel.onIntent(
                                OrderCheckoutIntent.OnIncreaseQuantity(
                                    foodItemId = foodItemId
                                )
                            )
                        },
                        onDecreaseQuantity = { foodItemId ->
                            viewModel.onIntent(
                                OrderCheckoutIntent.OnDecreaseQuantity(
                                    foodItemId = foodItemId
                                )
                            )
                        },
                        onRemoveItem = { foodItemId ->
                            viewModel.onIntent(
                                OrderCheckoutIntent.OnRemoveItem(
                                    foodItemId = foodItemId
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CheckoutHeaderText(
                        text = stringResource(Res.string.comments).uppercase(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    BasicTextField(
                        value = checkoutState.comments,
                        onValueChange = {
                            viewModel.onIntent(OrderCheckoutIntent.UpdateComment(it))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                shape = MaterialTheme.shapes.large,
                                color = MaterialTheme.colorScheme.surfaceHighest
                            )
                            .imePadding()
                            .padding(
                                vertical = 12.dp,
                                horizontal = 20.dp
                            ),
                        minLines = 3,
                        textStyle = MaterialTheme.typography.body2Regular.copy(
                            color = MaterialTheme.colorScheme.textPrimary
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if (checkoutState.comments.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.add_comment),
                                        style = MaterialTheme.typography.body2Regular,
                                        color = MaterialTheme.colorScheme.textSecondary,
                                        modifier = Modifier
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            OrderTotal(
                total = checkoutState.total,
                modifier = Modifier
                    .offset(
                        y = (-12).dp
                    ),
                enablePlaceOrder = checkoutState.hasValidCart,
                onPlaceOrder = {
                    viewModel.onIntent(OrderCheckoutIntent.PlaceOrder)
                }
            )
        }
    }

    val pickupTime = checkoutState.pickupTime
    if (pickupTime is PickupTime.ScheduledTime) {
        if (pickupTime.date == null) {
            SelectDateDialog(
                onDateSelected = {
                    val intent = OrderCheckoutIntent.SetPickupTime(
                        PickupTime.ScheduledTime(
                            date = it,
                            time = null
                        )
                    )
                    viewModel.onIntent(intent)
                },
                onCancel = {
                    val intent = OrderCheckoutIntent.SetPickupTime(
                        PickupTime.EarliestAvailableTime
                    )
                    viewModel.onIntent(intent)
                }
            )
        } else {
            if (pickupTime.time == null) {
                SelectTimeDialog(
                    onTimeSelected = { hour, minute ->
                        val intent = OrderCheckoutIntent.SetPickupTime(
                            PickupTime.ScheduledTime(
                                date = pickupTime.date,
                                time = "$hour:$minute"
                            )
                        )
                        viewModel.onIntent(intent)
                    },
                    onCancel = {
                        val intent = OrderCheckoutIntent.SetPickupTime(
                            PickupTime.EarliestAvailableTime
                        )
                        viewModel.onIntent(intent)
                    }
                )
            }
        }
    }
}

