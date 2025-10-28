package com.seenu.dev.android.lazypizza.domain.model

data class FoodItem constructor(
    val id: String,
    val name: String,
    val type: FoodType,
    val ingredients: List<String>,
    val price: Double,
    val imageUrl: String,
)

enum class FoodType(val sortOrder: Int) {
    PIZZA(1), DRINK(2), SAUCE(4), ICE_CREAM(3)
}

data class FoodItemWithCount(
    val foodItem: FoodItem,
    val count: Int,
)