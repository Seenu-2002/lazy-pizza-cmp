package com.seenu.dev.android.lazypizza.presentation.state

data class OrderHistoryUiModel constructor(
    val id: String,
    val items: List<OrderItemUiModel>,
    val status: OrderStatus,
    val dateFormatted: String,
) {
    val totalPrice: Double
        get() = items.sumOf { it.price * it.quantity }
}

enum class OrderStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

data class OrderItemUiModel(
    val name: String,
    val quantity: Int,
    val price: Double
)