package com.seenu.dev.android.lazypizza.data.mappers

import com.seenu.dev.android.lazypizza.Cart_food_item
import com.seenu.dev.android.lazypizza.Cart_food_item__topping
import com.seenu.dev.android.lazypizza.Cart_topping
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItemWithCount
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.domain.model.Topping

fun Cart_food_item.toFoodItemWithCount(): FoodItemWithCount {
    val foodItem = FoodItem(
        id = this.id,
        name = this.name,
        type = FoodType.valueOf(this.type),
        ingredients = this.ingredients.split(","),
        price = this.price,
        imageUrl = this.image_url,
    )

    return FoodItemWithCount(
        foodItem = foodItem,
        count = this.quantity.toInt()
    )
}

fun Cart_topping.toTopping(): Topping {
    return Topping(
        id = this.id,
        name = this.name,
        price = this.price,
        imageUrl = this.image_url
    )
}