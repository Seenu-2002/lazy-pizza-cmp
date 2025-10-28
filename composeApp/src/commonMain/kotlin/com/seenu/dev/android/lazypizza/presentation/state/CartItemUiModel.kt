package com.seenu.dev.android.lazypizza.presentation.state

data class CartItemUiModel constructor(
    val foodItem: FoodItemUiModel,
    val toppings: List<ToppingUiModel> = emptyList()
) {

    val itemTotalPrice: Double = run {
        val toppingsTotal = toppings.sumOf { it.price * it.countInCart }
        (foodItem.price * foodItem.countInCart) + toppingsTotal
    }

    val singleItemPrice: Double = run {
        val toppingsTotal = toppings.sumOf { it.price }
        foodItem.price + toppingsTotal
    }

}