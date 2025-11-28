package com.seenu.dev.android.lazypizza.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import co.touchlab.kermit.Logger.Companion.d
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.LazyPizzaDatabase
import com.seenu.dev.android.lazypizza.data.dto.OrderInfoDto
import com.seenu.dev.android.lazypizza.data.mappers.toDomain
import com.seenu.dev.android.lazypizza.data.mappers.toDto
import com.seenu.dev.android.lazypizza.data.mappers.toFoodItemWithCount
import com.seenu.dev.android.lazypizza.data.remote.RemoteCartDataSource
import com.seenu.dev.android.lazypizza.domain.OrderHistoryItem
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.CartItemLite
import com.seenu.dev.android.lazypizza.domain.model.OrderData
import com.seenu.dev.android.lazypizza.domain.model.OrderInfo
import com.seenu.dev.android.lazypizza.domain.model.Topping
import com.seenu.dev.android.lazypizza.domain.model.ToppingWithCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CartRepositoryImpl constructor(
    database: LazyPizzaDatabase,
    val remoteCartDataSource: RemoteCartDataSource
) : LazyPizzaCartRepository {

    private val queries = database.lazyPizzaDatabaseQueries

    override suspend fun getCartItems(): List<CartItem> {
        return withContext(Dispatchers.IO) {
            queries.getAllCartFoodItems()
                .executeAsList()
                .map { item ->
                    val foodItemWithCount = item.toFoodItemWithCount()
                    val toppingsWithCount =
                        queries.getToppingsForFoodItemWithCount(item.id)
                            .executeAsList()
                            .map { item ->
                                val topping = Topping(
                                    id = item.id,
                                    name = item.name,
                                    price = item.price,
                                    imageUrl = item.image_url
                                )

                                ToppingWithCount(
                                    topping = topping,
                                    count = item.topping_quantity.toInt()
                                )
                            }

                    CartItem(
                        foodItemWithCount = foodItemWithCount,
                        toppingsWithCount = toppingsWithCount
                    )
                }
        }
    }

    override suspend fun syncCartItems() {
        return withContext(Dispatchers.IO) {
            val localCartItems = getCartItems()
                .map { it }

            if (localCartItems.isNotEmpty()) {
                remoteCartDataSource.clearCart()
                for (item in localCartItems) {
                    remoteCartDataSource.addItemToCart(item)
                }
                return@withContext
            } else {
                val cartItems = remoteCartDataSource.getCartItems()
                d {
                    "Fetched ${cartItems.size} cart items from remote source"
                }
                queries.transaction {
                    queries.clearCartFoodItems()
                    queries.clearCartToppings()
                    queries.clearCartFoodItemToppings()

                    for (cartItem in cartItems) {
                        queries.insertCartFoodItem(
                            id = cartItem.foodItemWithCount.foodItem.id,
                            name = cartItem.foodItemWithCount.foodItem.name,
                            type = cartItem.foodItemWithCount.foodItem.type.name,
                            ingredients = cartItem.foodItemWithCount.foodItem.ingredients.joinToString(
                                ","
                            ),
                            price = cartItem.foodItemWithCount.foodItem.price,
                            image_url = cartItem.foodItemWithCount.foodItem.imageUrl,
                            quantity = cartItem.foodItemWithCount.count.toLong()
                        )

                        for (toppingWithCount in cartItem.toppingsWithCount) {
                            queries.insertCartTopping(
                                id = toppingWithCount.topping.id,
                                name = toppingWithCount.topping.name,
                                price = toppingWithCount.topping.price,
                                image_url = toppingWithCount.topping.imageUrl
                            )

                            queries.insertCartFoodItemTopping(
                                food_item_id = cartItem.foodItemWithCount.foodItem.id,
                                topping_id = toppingWithCount.topping.id,
                                topping_quantity = toppingWithCount.count.toLong()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getCartItemsFlow(): Flow<List<CartItem>> {
        return flow {
            queries.getAllCartFoodItemToppings()
                .asFlow()
                .mapToList(Dispatchers.IO)
                .collect { cartFoodItemToppings ->
                    val cartItems = queries.getAllCartFoodItems()
                        .executeAsList()
                        .map { item ->
                            val foodItemWithCount = item.toFoodItemWithCount()
                            val toppingsWithCount =
                                queries.getToppingsForFoodItemWithCount(item.id)
                                    .executeAsList()
                                    .map { item ->
                                        val topping = Topping(
                                            id = item.id,
                                            name = item.name,
                                            price = item.price,
                                            imageUrl = item.image_url
                                        )

                                        ToppingWithCount(
                                            topping = topping,
                                            count = item.topping_quantity.toInt()
                                        )
                                    }

                            CartItem(
                                foodItemWithCount = foodItemWithCount,
                                toppingsWithCount = toppingsWithCount
                            )
                        }
                    emit(cartItems)
                }
        }
    }

    override fun getCartItemsCountFlow(): Flow<Int> {
        return queries.getCartItemsCount()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { it.count?.toInt() ?: 0 }
    }

    override fun getFoodItemIdsInCartFlow(): Flow<List<CartItemLite>> {
        return queries.getFoodItemIdsInCart()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { items -> items.map { CartItemLite(it.id, it.quantity.toInt()) } }
    }

    override suspend fun updateCartItems(items: List<CartItem>) {
        queries.transaction {
            for (cartItem in items) {
                queries.insertCartFoodItem(
                    id = cartItem.foodItemWithCount.foodItem.id,
                    name = cartItem.foodItemWithCount.foodItem.name,
                    type = cartItem.foodItemWithCount.foodItem.type.name,
                    ingredients = cartItem.foodItemWithCount.foodItem.ingredients.joinToString(","),
                    price = cartItem.foodItemWithCount.foodItem.price,
                    image_url = cartItem.foodItemWithCount.foodItem.imageUrl,
                    quantity = cartItem.foodItemWithCount.count.toLong()
                )

                for (toppingWithCount in cartItem.toppingsWithCount) {
                    queries.insertCartTopping(
                        id = toppingWithCount.topping.id,
                        name = toppingWithCount.topping.name,
                        price = toppingWithCount.topping.price,
                        image_url = toppingWithCount.topping.imageUrl
                    )

                    queries.insertCartFoodItemTopping(
                        food_item_id = cartItem.foodItemWithCount.foodItem.id,
                        topping_id = toppingWithCount.topping.id,
                        topping_quantity = toppingWithCount.count.toLong()
                    )
                }
            }
        }

        remoteCartDataSource.updateCart(items)
    }

    override suspend fun addItemToCart(item: CartItem) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                queries.insertCartFoodItem(
                    id = item.foodItemWithCount.foodItem.id,
                    name = item.foodItemWithCount.foodItem.name,
                    type = item.foodItemWithCount.foodItem.type.name,
                    ingredients = item.foodItemWithCount.foodItem.ingredients.joinToString(","),
                    price = item.foodItemWithCount.foodItem.price,
                    image_url = item.foodItemWithCount.foodItem.imageUrl,
                    quantity = item.foodItemWithCount.count.toLong()
                )

                for (toppingWithCount in item.toppingsWithCount) {
                    queries.insertCartTopping(
                        id = toppingWithCount.topping.id,
                        name = toppingWithCount.topping.name,
                        price = toppingWithCount.topping.price,
                        image_url = toppingWithCount.topping.imageUrl
                    )

                    queries.insertCartFoodItemTopping(
                        food_item_id = item.foodItemWithCount.foodItem.id,
                        topping_id = toppingWithCount.topping.id,
                        topping_quantity = toppingWithCount.count.toLong()
                    )
                }
            }

            remoteCartDataSource.addItemToCart(item)
        }
    }

    override suspend fun updateItemInCart(item: CartItem) {
        withContext(Dispatchers.IO) {
            queries.updateCartFoodItemQuantity(
                id = item.foodItemWithCount.foodItem.id,
                quantity = item.foodItemWithCount.count.toLong()
            )

            remoteCartDataSource.updateItemInCart(item)
        }
    }

    override suspend fun removeItemFromCart(itemId: String) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                queries.removeCartFoodItemById(itemId)
                queries.removeCartToppingsForFoodItem(itemId)
            }

            remoteCartDataSource.removeItemFromCart(itemId)
        }
    }

    override suspend fun checkout(data: OrderData): OrderInfo {
        return withContext(Dispatchers.IO) {
            val items = getCartItems()
            val info = remoteCartDataSource.checkout(data.toDto(), items).toDomain()
            clearCart()
            info
        }
    }

    override suspend fun getOrders(): List<OrderHistoryItem> {
        return withContext(Dispatchers.IO) {
            remoteCartDataSource.getOrders()
                .map { it.toDomain() }
        }
    }

    override suspend fun clearCart(syncWithRemote: Boolean) {
        return withContext(Dispatchers.IO) {
            queries.transaction {
                queries.clearCartFoodItems()
                queries.clearCartToppings()
                queries.clearCartFoodItemToppings()
            }

            if (syncWithRemote) {
                remoteCartDataSource.clearCart()
            }
        }
    }

}