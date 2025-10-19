package com.seenu.dev.android.lazypizza.data.repository

import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.Topping

interface LazyPizzaRepository {
    suspend fun getFoodItems(): List<FoodItem>
    suspend fun getFoodItemById(id: Long): FoodItem?
    suspend fun getToppings(): List<Topping>
}