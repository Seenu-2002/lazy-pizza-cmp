package com.seenu.dev.android.lazypizza.presentation.state

data class FoodItemUiModel constructor(
    val id: Long,
    val name: String,
    val type: FoodType,
    val ingredients: String?,
    val prize: Double,
    val countInCart: Int = 0,
)

enum class FoodType {
    PIZZA, DRINK, SAUCE, ICE_CREAM
}