package com.seenu.dev.android.lazypizza.domain.model

data class CartItem constructor(
    val foodItemWithCount: FoodItemWithCount,
    val toppingsWithCount: List<ToppingWithCount> = emptyList()
)