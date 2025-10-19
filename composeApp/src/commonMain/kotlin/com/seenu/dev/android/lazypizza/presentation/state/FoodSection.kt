package com.seenu.dev.android.lazypizza.presentation.state

import com.seenu.dev.android.lazypizza.domain.model.FoodType

data class FoodSection constructor(
    val type: FoodType,
    val items: List<FoodItemUiModel>
)