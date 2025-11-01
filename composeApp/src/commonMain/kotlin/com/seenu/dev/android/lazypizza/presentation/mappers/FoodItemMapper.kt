package com.seenu.dev.android.lazypizza.presentation.mappers

import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.Topping
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel

fun FoodItem.toUiModel(countInCart: Int = 0) = FoodItemUiModel(
    id = id,
    name = name,
    type = type,
    ingredients = ingredients,
    ingredientsFormatted = this.ingredients.joinToString(", "),
    price = price,
    imageUrl = imageUrl,
    countInCart = countInCart
)

fun FoodItemUiModel.toDomain(): FoodItem = FoodItem(
    id = id,
    name = name,
    type = type,
    ingredients = ingredients,
    price = price,
    imageUrl = imageUrl
)

fun Topping.toUiModel(countInCart: Int = 0) = ToppingUiModel(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl,
    countInCart = countInCart
)

fun ToppingUiModel.toDomain(): Topping = Topping(
    id = id,
    name = name,
    price = price,
    imageUrl = imageUrl
)