package com.seenu.dev.android.lazypizza.presentation.state

data class FoodSection constructor(
    val type: FoodType,
    val items: List<FoodItemUiModel>
)