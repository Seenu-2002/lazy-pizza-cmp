package com.seenu.dev.android.lazypizza.presentation.mappers

import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItemWithCount
import com.seenu.dev.android.lazypizza.domain.model.Topping
import com.seenu.dev.android.lazypizza.domain.model.ToppingWithCount
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel

fun CartItem.toUiModel(): CartItemUiModel {
    val foodItemUiModel =
        foodItemWithCount.foodItem.toUiModel(countInCart = foodItemWithCount.count)
    val toppingsUiModel = toppingsWithCount.map { it.topping.toUiModel(countInCart = it.count) }
    return CartItemUiModel(
        foodItem = foodItemUiModel,
        toppings = toppingsUiModel
    )
}

fun CartItemUiModel.toDomain(): CartItem {
    return CartItem(
        foodItemWithCount = this.foodItem.toDomainWithCount(),
        toppingsWithCount = this.toppings.map { it.toDomainWithCount() }
    )
}

fun FoodItemUiModel.toDomainWithCount(): FoodItemWithCount {
    return FoodItemWithCount(
        foodItem = FoodItem(
            id = this.id,
            name = this.name,
            price = this.price,
            imageUrl = this.imageUrl,
            type = this.type,
            ingredients = this.ingredients
        ),
        count = this.countInCart
    )
}

fun ToppingUiModel.toDomainWithCount(): ToppingWithCount {
    return ToppingWithCount(
        Topping(
            id = this.id,
            name = this.name,
            price = this.price,
            imageUrl = this.imageUrl
        ), count = this.countInCart
    )
}