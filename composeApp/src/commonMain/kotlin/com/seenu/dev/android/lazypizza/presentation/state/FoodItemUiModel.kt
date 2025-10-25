package com.seenu.dev.android.lazypizza.presentation.state

import com.seenu.dev.android.lazypizza.domain.model.FoodType

data class FoodItemUiModel constructor(
    val id: String,
    val name: String,
    val type: FoodType,
    val ingredients: String?,
    val price: Double,
    val imageUrl: String,
    val countInCart: Int = 0,
)