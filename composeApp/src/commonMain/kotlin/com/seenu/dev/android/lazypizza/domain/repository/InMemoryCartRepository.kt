package com.seenu.dev.android.lazypizza.domain.repository

import co.touchlab.kermit.Logger
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

//class InMemoryCartRepository : LazyPizzaCartRepository {
//
//    private val cartItems = mutableListOf<CartItem>()
//
//    override suspend fun getCartItems(): List<CartItem> {
//        Logger.d { ":: getCartItems" }
//        return cartItems.toList()
//    }
//
//    override suspend fun updateCartItems(items: List<CartItem>) {
//        Logger.d { ":: updateCartItems" }
//        cartItems.clear()
//        for (item in items) {
//            addItemToCart(item)
//        }
//    }
//
//    override suspend fun addItemToCart(item: CartItem) {
//        Logger.d { ":: addItemToCart" }
//        val existingItem =
//            cartItems.find { it.foodItemWithCount.foodItem.id == item.foodItemWithCount.foodItem.id }
//        if (existingItem != null) {
//            val index = cartItems.indexOf(existingItem)
//            val updatedItem = existingItem.copy(
//                foodItemWithCount = existingItem.foodItemWithCount.copy(
//                    count = existingItem.foodItemWithCount.count + existingItem.foodItemWithCount.count
//                )
//            )
//            cartItems[index] = updatedItem
//        } else {
//            cartItems.add(item)
//        }
//    }
//
//    override suspend fun updateItemInCart(item: CartItem) {
//        Logger.d { ":: updateItemInCart" }
//        val existingItem =
//            cartItems.find { it.foodItemWithCount.foodItem.id == item.foodItemWithCount.foodItem.id }
//        if (existingItem != null) {
//            val index = cartItems.indexOf(existingItem)
//            cartItems[index] = item
//        }
//    }
//
//    override suspend fun removeItemFromCart(itemId: String) {
//        Logger.d { ":: removeItemFromCart" }
//        cartItems.removeAll { it.foodItemWithCount.foodItem.id == itemId }
//    }
//
//    override suspend fun clearCart() {
//        Logger.d { ":: clearCart" }
//        cartItems.clear()
//    }
//}