package com.seenu.dev.android.lazypizza.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.presentation.state.OrderHistoryUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderStatus
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class OrderHistoryViewModel : ViewModel() {

    private val auth = Firebase.auth

    private val _orderHistoryUiState: MutableStateFlow<OrderHistoryState> = MutableStateFlow(
        OrderHistoryState()
    )
    val orderHistoryUiState: StateFlow<OrderHistoryState> = _orderHistoryUiState
        .onStart {
            checkUserLoggedIn()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = OrderHistoryState()
        )

    private fun checkUserLoggedIn() {
        val isLoggedIn = auth.currentUser != null
        _orderHistoryUiState.value = _orderHistoryUiState.value.copy(
            isUserLoggedIn = isLoggedIn,
            orders = getMockOrdersData()
        )
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
    val isUserLoggedIn: Boolean = false,
    val orders: List<OrderHistoryUiModel> = emptyList()
)