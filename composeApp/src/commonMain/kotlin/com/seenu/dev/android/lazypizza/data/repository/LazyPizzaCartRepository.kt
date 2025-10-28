package com.seenu.dev.android.lazypizza.data.repository

import com.seenu.dev.android.lazypizza.domain.model.CartItem

interface LazyPizzaCartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun updateCartItems(items: List<CartItem>)
    suspend fun addItemToCart(item: CartItem)
    suspend fun updateItemInCart(item: CartItem)
    suspend fun removeItemFromCart(itemId: String)
    suspend fun clearCart()
}