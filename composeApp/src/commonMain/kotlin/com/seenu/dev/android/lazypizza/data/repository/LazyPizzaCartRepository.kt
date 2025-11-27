package com.seenu.dev.android.lazypizza.data.repository

import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.CartItemLite
import kotlinx.coroutines.flow.Flow

interface LazyPizzaCartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun syncCartItems()
    fun getCartItemsFlow(): Flow<List<CartItem>>
    fun getCartItemsCountFlow(): Flow<Int>
    fun getFoodItemIdsInCartFlow(): Flow<List<CartItemLite>>
    suspend fun updateCartItems(items: List<CartItem>)
    suspend fun addItemToCart(item: CartItem)
    suspend fun updateItemInCart(item: CartItem)
    suspend fun removeItemFromCart(itemId: String)
    suspend fun clearCart(syncWithRemote: Boolean = false)
}