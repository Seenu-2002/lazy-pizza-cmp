package com.seenu.dev.android.lazypizza.presentation.mappers

import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel

fun CartItem.toUiModel(): CartItemUiModel {
    val foodItemUiModel = foodItemWithCount.foodItem.toUiModel(countInCart = foodItemWithCount.count)
    val toppingsUiModel = toppingsWithCount.map { it.topping.toUiModel(countInCart = it.count) }
    return CartItemUiModel(
        foodItemUiModel = foodItemUiModel,
        toppingsUiModel = toppingsUiModel
    )
}