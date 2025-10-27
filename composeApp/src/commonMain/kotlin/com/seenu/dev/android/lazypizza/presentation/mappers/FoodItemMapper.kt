package com.seenu.dev.android.lazypizza.presentation.mappers

import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.Topping
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel

fun FoodItem.toUiModel(countInCart: Int = 0) = FoodItemUiModel(
    id = id,
    name = name,
    type = type,
    ingredients = this.ingredients.joinToString(", "),
    price = price,
    imageUrl = imageUrl,
    countInCart = countInCart
)

fun Topping.toUiModel(countInCart: Int = 0) = ToppingUiModel(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl,
    countInCart = countInCart
)