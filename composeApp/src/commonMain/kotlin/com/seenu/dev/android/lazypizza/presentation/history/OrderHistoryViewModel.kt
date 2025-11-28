package com.seenu.dev.android.lazypizza.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.presentation.state.OrderHistoryUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderStatus
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class OrderHistoryViewModel constructor(
    private val repository: LazyPizzaRepository,
    private val cartRepository: LazyPizzaCartRepository
) : ViewModel() {

    private val auth = Firebase.auth

    private val _orderHistoryUiState: MutableStateFlow<OrderHistoryState> = MutableStateFlow(
        OrderHistoryState()
    )
    val orderHistoryUiState: StateFlow<OrderHistoryState> = _orderHistoryUiState
        .onStart {
            checkUserLoggedIn()
            getOrders()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = OrderHistoryState()
        )

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

    private fun checkUserLoggedIn() {
        val isLoggedIn = auth.currentUser != null
        _orderHistoryUiState.value = _orderHistoryUiState.value.copy(
            isUserLoggedIn = isLoggedIn,
            orders = emptyList()
        )
    }

    @OptIn(ExperimentalTime::class)
    private fun getOrders() {
        viewModelScope.launch {
            _orderHistoryUiState.value = _orderHistoryUiState.value.copy(
                isLoading = true
            )
            val foodItemsDeferred = async {
                repository.getFoodItems()
            }
            val toppingsDeferred = async {
                repository.getToppings()
            }
            val ordersDeferred = async {
                cartRepository.getOrders()
            }
            val foodItems = foodItemsDeferred.await()
            val toppings = toppingsDeferred.await()
            val orders = ordersDeferred.await()
            val ordersUiModel = orders.map { order ->
                OrderHistoryUiModel(
                    id = order.orderId,
                    items = order.items.map { orderItem ->
                        val foodItem =
                            foodItems.firstOrNull { it.id == orderItem.foodItemId }
                        OrderItemUiModel(
                            name = foodItem?.name ?: "Unknown Item",
                            quantity = orderItem.quantity,
                            price = (foodItem?.price
                                ?: 0.0) + orderItem.toppings.sumOf { topping ->
                                toppings.firstOrNull { it.id == topping.toppingId }?.price ?: 0.0
                            }
                        )
                    },
                    status = OrderStatus.IN_PROGRESS,
                    dateFormatted = dateFormatter.format(
                        order.orderedAt.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
                    )
                )
            }
            _orderHistoryUiState.value = _orderHistoryUiState.value.copy(
                isLoading = false,
                orders = ordersUiModel
            )
        }
    }

    private fun getMockOrdersData(): List<OrderHistoryUiModel> {
        return listOf<OrderHistoryUiModel>(
            OrderHistoryUiModel(
                id = "Order #123",
                items = listOf(
                    OrderItemUiModel(
                        name = "Margherita",
                        quantity = 1,
                        price = 8.99
                    )
                ),
                status = OrderStatus.IN_PROGRESS,
                dateFormatted = "September 25, 12:15"
            ), OrderHistoryUiModel(
                id = "Order #12345",
                items = listOf(
                    OrderItemUiModel(
                        name = "Margherita",
                        quantity = 1,
                        price = 8.99
                    ),
                    OrderItemUiModel(
                        name = "Pepsi",
                        quantity = 2,
                        price = 12.49
                    ),
                    OrderItemUiModel(
                        name = "Cookies Ice Crean",
                        quantity = 1,
                        price = 5.99
                    )
                ),
                status = OrderStatus.COMPLETED,
                dateFormatted = "September 25, 12:15"
            ), OrderHistoryUiModel(
                id = "Order #12345",
                items = listOf(
                    OrderItemUiModel(
                        name = "Margherita",
                        quantity = 1,
                        price = 8.99
                    ),
                    OrderItemUiModel(
                        name = "Cookies Ice Crean",
                        quantity = 2,
                        price = 5.99
                    )
                ),
                status = OrderStatus.COMPLETED,
                dateFormatted = "September 25, 12:15"
            )
        )
    }

}

data class OrderHistoryState(
    val isLoading: Boolean = true,
    val isUserLoggedIn: Boolean = false,
    val orders: List<OrderHistoryUiModel> = emptyList()
)