package com.seenu.dev.android.lazypizza.data.mappers

import com.seenu.dev.android.lazypizza.data.dto.OrderDataDto
import com.seenu.dev.android.lazypizza.data.dto.OrderFoodItemDto
import com.seenu.dev.android.lazypizza.data.dto.OrderHistoryDto
import com.seenu.dev.android.lazypizza.data.dto.OrderInfoDto
import com.seenu.dev.android.lazypizza.data.dto.OrderToppingItemDto
import com.seenu.dev.android.lazypizza.domain.OrderFoodItem
import com.seenu.dev.android.lazypizza.domain.OrderHistoryItem
import com.seenu.dev.android.lazypizza.domain.OrderToppingItem
import com.seenu.dev.android.lazypizza.domain.model.OrderData
import com.seenu.dev.android.lazypizza.domain.model.OrderInfo
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun OrderInfoDto.toDomain() = OrderInfo(
    id = id,
    time = Instant.fromEpochSeconds(time)
)

@OptIn(ExperimentalTime::class)
fun OrderData.toDto() = OrderDataDto(
    id = id,
    time = time.toEpochMilliseconds(),
    comments = comments
)

@OptIn(ExperimentalTime::class)
fun OrderHistoryDto.toDomain() = OrderHistoryItem(
    orderId = orderId,
    orderedAt = Instant.fromEpochSeconds(orderedAt),
    comments = comments,
    items = items.map { itemDto -> itemDto.toDomain() }
)

fun OrderFoodItemDto.toDomain() = OrderFoodItem(
    foodItemId = foodItemId,
    quantity = quantity,
    toppings = toppings.map { it.toDomain() }
)

fun OrderToppingItemDto.toDomain() = OrderToppingItem(
    toppingId = toppingId,
    quantity = quantity
)