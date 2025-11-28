package com.seenu.dev.android.lazypizza.domain

import com.seenu.dev.android.lazypizza.data.dto.OrderFoodItemDto
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class OrderHistoryItem @OptIn(ExperimentalTime::class) constructor(
    val orderId: String,
    val orderedAt: Instant,
    val comments: String,
    val items: List<OrderFoodItem>,
)

data class OrderFoodItem constructor(
    val foodItemId: String,
    val quantity: Int,
    val toppings: List<OrderToppingItem>,
)

data class OrderToppingItem constructor(
    val toppingId: String,
    val quantity: Int
)