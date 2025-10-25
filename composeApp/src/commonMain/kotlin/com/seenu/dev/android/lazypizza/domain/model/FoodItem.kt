package com.seenu.dev.android.lazypizza.domain.model

data class FoodItem constructor(
    val id: String,
    val name: String,
    val type: FoodType,
    val ingredients: List<String>,
    val price: Double,
    val imageUrl: String,
)

enum class FoodType {
    PIZZA, DRINK, SAUCE, ICE_CREAM
}