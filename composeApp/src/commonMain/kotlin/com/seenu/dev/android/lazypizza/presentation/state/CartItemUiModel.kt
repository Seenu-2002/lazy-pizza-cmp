package com.seenu.dev.android.lazypizza.presentation.state

data class CartItemUiModel constructor(
    val foodItemUiModel: FoodItemUiModel,
    val toppingsUiModel: List<ToppingUiModel> = emptyList()
)