package com.seenu.dev.android.lazypizza.data.mappers

import com.seenu.dev.android.lazypizza.data.dto.FoodItemDto
import com.seenu.dev.android.lazypizza.data.dto.ToppingDto
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.domain.model.Topping

fun FoodItemDto.toDomain(): FoodItem {
    return FoodItem(
        id = id,
        name = name,
        type = FoodType.valueOf(type.uppercase()),
        ingredients = ingredients,
        price = price,
        imageUrl = imageUrl
    )
}

fun ToppingDto.toDomain(): Topping {
    return Topping(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}