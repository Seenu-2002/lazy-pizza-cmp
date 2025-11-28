package com.seenu.dev.android.lazypizza.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItemWithCount
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.domain.model.OrderData
import com.seenu.dev.android.lazypizza.presentation.mappers.toDomain
import com.seenu.dev.android.lazypizza.presentation.mappers.toUiModel
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderConfirmationUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class OrderCheckoutViewModel constructor(
    private val repository: LazyPizzaRepository,
    private val cartRepository: LazyPizzaCartRepository
) : ViewModel() {

    private val _checkoutUiState: MutableStateFlow<CheckoutUiState> = MutableStateFlow(
        CheckoutUiState()
    )

    val checkoutUiState: StateFlow<CheckoutUiState> = _checkoutUiState
        .onStart {
            viewModelScope.launch {
                fetchRecommendedItems()
                fetchCartItems()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CheckoutUiState()
        )

    private var _generatedSuggestions: List<FoodItemUiModel> = emptyList()

    @OptIn(FormatStringsInDatetimeFormats::class)
    private val timeFormatter = LocalTime.Format {
        hour()
        char(':')
        minute()
    }
    @OptIn(FormatStringsInDatetimeFormats::class)
    private val dateFormatter = LocalDateTime.Format {
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        day()
        char(',')
        char(' ')
        hour()
        char(':')
        minute()
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun fetchCartItems() {
        _checkoutUiState.update { it.copy(isLoading = true) }

        val earliestPickupTime = calculateEarliestAvailableTime()
        cartRepository.getCartItemsFlow().collect { cartItems ->
            _checkoutUiState.emit(
                _checkoutUiState.value.copy(
                    isLoading = false,
                    cartItems = cartItems.map { it.toUiModel() },
                    recommendedItems = _generatedSuggestions.filter { suggestion ->
                        !cartItems.any { it.foodItemWithCount.foodItem.id == suggestion.id }
                    },
                    earliestPickupTime = earliestPickupTime,
                    earliestPickupTimeLabel = timeFormatter.format(earliestPickupTime.time)
                )
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun calculateEarliestAvailableTime(): LocalDateTime {
        return (Clock.System.now() + 15.minutes).toLocalDateTime(
            TimeZone.currentSystemDefault()
        )
    }

    private suspend fun fetchRecommendedItems() {
        val foodItems = repository.getFoodItems()
        _generatedSuggestions = foodItems
            .filter { it.type == FoodType.SAUCE || it.type == FoodType.DRINK }
            .map { item -> item.toUiModel() }
            .shuffled()
    }

    @OptIn(ExperimentalTime::class)
    fun onIntent(intent: OrderCheckoutIntent) {
        viewModelScope.launch {
            when (intent) {
                is OrderCheckoutIntent.SetPickupTime -> {
                    when (intent.pickupTime) {
                        is PickupTime.ScheduledTime -> {
                            val date =
                                if (intent.pickupTime.date != null && intent.pickupTime.time != null) {
                                    Instant.fromEpochSeconds(intent.pickupTime.date)
                                        .toLocalDateTime(
                                            timeZone = TimeZone.currentSystemDefault()
                                        ).let {
                                        LocalDateTime(
                                            year = it.year,
                                            month = it.month,
                                            day = it.day,
                                            hour = intent.pickupTime.time.substringBefore(":")
                                                .toInt(),
                                            minute = intent.pickupTime.time.substringAfter(":")
                                                .toInt()
                                        )
                                    }
                                } else {
                                    _checkoutUiState.value.earliestPickupTime
                                }
                            val earliestPickupTimeLabel = if (date != null) {
                                dateFormatter.format(date)
                            } else {
                                ""
                            }
                            _checkoutUiState.emit(
                                _checkoutUiState.value.copy(
                                    pickupTime = intent.pickupTime,
                                    earliestPickupTime = date,
                                    earliestPickupTimeLabel = earliestPickupTimeLabel
                                )
                            )
                        }

                        else -> {
                            val earliestPickupTime = calculateEarliestAvailableTime()
                            _checkoutUiState.emit(
                                _checkoutUiState.value.copy(
                                    earliestPickupTime = earliestPickupTime,
                                    pickupTime = intent.pickupTime,
                                    earliestPickupTimeLabel = timeFormatter.format(
                                        earliestPickupTime.time
                                    )
                                )
                            )
                        }
                    }
                }

                is OrderCheckoutIntent.PlaceOrder -> {
                    val data = OrderData(
                        id = (Clock.System.now().toEpochMilliseconds() % 100_000).toString(),
                        time = _checkoutUiState.value.earliestPickupTime!!.toInstant(
                            timeZone = TimeZone.currentSystemDefault()
                        ),
                        comments = _checkoutUiState.value.comments
                    )
                    val info = cartRepository.checkout(data)
                    _checkoutUiState.emit(
                        _checkoutUiState.value.copy(
                            confirmation = info.toUiModel()
                        )
                    )
                }

                is OrderCheckoutIntent.OnExpandOrderDetails -> {
                    _checkoutUiState.emit(
                        _checkoutUiState.value.copy(isOrderDetailsExpanded = intent.isExpanded)
                    )
                }

                is OrderCheckoutIntent.UpdateComment -> {
                    _checkoutUiState.emit(
                        _checkoutUiState.value.copy(comments = intent.comments)
                    )
                }

                is OrderCheckoutIntent.AddItemToCart -> {
                    val foodItem = _generatedSuggestions.find { it.id == intent.foodItemId }
                        ?: return@launch
                    val cartItem = CartItem(
                        foodItemWithCount = FoodItemWithCount(
                            foodItem = foodItem.toDomain(),
                            count = 1
                        )
                    )
                    cartRepository.addItemToCart(cartItem)
                }

                is OrderCheckoutIntent.OnIncreaseQuantity -> {
                    val cartItem = _checkoutUiState.value.cartItems.find {
                        it.foodItem.id == intent.foodItemId
                    }?.toDomain() ?: return@launch
                    cartRepository.updateItemInCart(
                        cartItem.copy(
                            foodItemWithCount = cartItem.foodItemWithCount.copy(
                                count = cartItem.foodItemWithCount.count + 1
                            )
                        )
                    )
                }

                is OrderCheckoutIntent.OnDecreaseQuantity -> {
                    val cartItem = _checkoutUiState.value.cartItems.find {
                        it.foodItem.id == intent.foodItemId
                    }?.toDomain() ?: return@launch
                    if (cartItem.foodItemWithCount.count > 1) {
                        cartRepository.updateItemInCart(
                            cartItem.copy(
                                foodItemWithCount = cartItem.foodItemWithCount.copy(
                                    count = cartItem.foodItemWithCount.count - 1
                                )
                            )
                        )
                    } else {
                        cartRepository.removeItemFromCart(cartItem.foodItemWithCount.foodItem.id)
                    }
                }

                is OrderCheckoutIntent.OnRemoveItem -> {
                    cartRepository.removeItemFromCart(intent.foodItemId)
                }
            }
        }
    }
}

sealed interface OrderCheckoutIntent {
    data class SetPickupTime(val pickupTime: PickupTime) : OrderCheckoutIntent
    data object PlaceOrder : OrderCheckoutIntent
    data class OnExpandOrderDetails(val isExpanded: Boolean) : OrderCheckoutIntent
    data class UpdateComment(val comments: String) : OrderCheckoutIntent
    data class AddItemToCart(val foodItemId: String) : OrderCheckoutIntent
    data class OnIncreaseQuantity(val foodItemId: String) : OrderCheckoutIntent
    data class OnDecreaseQuantity(val foodItemId: String) : OrderCheckoutIntent
    data class OnRemoveItem(val foodItemId: String) : OrderCheckoutIntent
}

data class CheckoutUiState constructor(
    val isLoading: Boolean = true,
    val earliestPickupTime: LocalDateTime? = null,
    val earliestPickupTimeLabel: String = "",
    val pickupTime: PickupTime = PickupTime.EarliestAvailableTime,
    val cartItems: List<CartItemUiModel> = emptyList(),
    val isOrderDetailsExpanded: Boolean = false,
    val comments: String = "",
    val recommendedItems: List<FoodItemUiModel> = emptyList(),
    val confirmation: OrderConfirmationUiModel? = null
) {
    val hasValidCart: Boolean
        get() {
            val validCartItems =
                cartItems.isNotEmpty() && cartItems.any { it.foodItem.countInCart > 0 }
            return if (pickupTime is PickupTime.ScheduledTime) {
                pickupTime.time != null && pickupTime.date != null && validCartItems
            } else {
                validCartItems
            }
        }

    val total: Double
        get() = cartItems.sumOf {
            val foodItemCost = it.foodItem.countInCart * it.foodItem.price
            val toppingsCost = it.toppings.sumOf { topping ->
                topping.countInCart * topping.price
            }
            foodItemCost + toppingsCost
        }
}

sealed interface PickupTime {
    data object EarliestAvailableTime : PickupTime
    data class ScheduledTime constructor(
        val date: Long? = null,
        val time: String? = null
    ) : PickupTime
}