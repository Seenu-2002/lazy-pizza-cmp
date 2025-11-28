package com.seenu.dev.android.lazypizza.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderHistoryDto constructor(
    @SerialName("order_id")
    val orderId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("order_time")
    val orderedAt: Long,
    val comments: String,
    val items: List<OrderFoodItemDto>
)

@Serializable
data class OrderFoodItemDto constructor(
    @SerialName("food_item_id")
    val foodItemId: String,
    val quantity: Int,
    val toppings: List<OrderToppingItemDto>
)

@Serializable
data class OrderToppingItemDto constructor(
    @SerialName("topping_id")
    val toppingId: String,
    val quantity: Int
)
