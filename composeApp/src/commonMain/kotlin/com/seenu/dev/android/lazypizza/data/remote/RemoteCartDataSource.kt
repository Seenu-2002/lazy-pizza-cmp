package com.seenu.dev.android.lazypizza.data.remote

import com.seenu.dev.android.lazypizza.data.dto.OrderDataDto
import com.seenu.dev.android.lazypizza.data.dto.OrderHistoryDto
import com.seenu.dev.android.lazypizza.data.dto.OrderInfoDto
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItemWithCount
import com.seenu.dev.android.lazypizza.domain.model.ToppingWithCount
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.WriteBatch
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

class RemoteCartDataSource constructor(
    private val repository: LazyPizzaRepository
) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    suspend fun getCartItems(): List<CartItem> {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext emptyList()
            val documents = firestore.collection("cart_items")
                .where {
                    "user_id" equalTo user.uid
                }
                .get()
                .documents

            val foodItemsDeferred = async {
                repository.getFoodItems()
            }
            val toppingsDeferred = async {
                repository.getToppings()
            }
            val foodItems = foodItemsDeferred.await()
            val toppings = toppingsDeferred.await()

            val items = documents.map { doc ->
                val cartItemRemoteDto = doc.data<CartItemRemoteDto>()
                val toppings = cartItemRemoteDto.toppings.map { toppingDto ->
                    val topping = toppings.first { it.id == toppingDto.toppingId }
                    ToppingWithCount(
                        topping = topping,
                        count = toppingDto.quantity
                    )
                }
                val foodItem = foodItems.first { it.id == cartItemRemoteDto.foodItemId }
                CartItem(
                    foodItemWithCount = FoodItemWithCount(
                        foodItem = foodItem,
                        count = cartItemRemoteDto.quantity
                    ),
                    toppingsWithCount = toppings
                )
            }
            items
        }
    }

    suspend fun updateCart(items: List<CartItem>) {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext
            val batch = firestore.batch()

            clearCartInternal(batch)

            for (item in items) {
                val docRef = firestore.collection("cart_items")
                    .document
                val data = mapOf(
                    "user_id" to user.uid,
                    "food_item_id" to item.foodItemWithCount.foodItem.id,
                    "quantity" to item.foodItemWithCount.count,
                    "toppings" to item.toppingsWithCount.map {
                        mapOf(
                            "topping_id" to it.topping.id,
                            "quantity" to it.count
                        )
                    }
                )
                batch.set(docRef, data)
            }
            batch.commit()
        }
    }

    suspend fun addItemToCart(item: CartItem) {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext
            val docRef = firestore.collection("cart_items")
                .document
            val data = mapOf(
                "user_id" to user.uid,
                "food_item_id" to item.foodItemWithCount.foodItem.id,
                "quantity" to item.foodItemWithCount.count,
                "toppings" to item.toppingsWithCount.map {
                    mapOf(
                        "topping_id" to it.topping.id,
                        "quantity" to it.count
                    )
                }
            )
            docRef.set(data)
        }
    }

    suspend fun updateItemInCart(item: CartItem) {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext
            val querySnapshot = firestore.collection("cart_items")
                .where {
                    "user_id" equalTo user.uid
                    "food_item_id" equalTo item.foodItemWithCount.foodItem.id
                }
                .get()
            val document = querySnapshot.documents.firstOrNull() ?: return@withContext
            val data = mapOf(
                "quantity" to item.foodItemWithCount.count,
                "toppings" to item.toppingsWithCount.map {
                    mapOf(
                        "topping_id" to it.topping.id,
                        "quantity" to it.count
                    )
                }
            )
            document.reference.update(data)
        }
    }

    suspend fun removeItemFromCart(itemId: String) {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext
            val querySnapshot = firestore.collection("cart_items")
                .where {
                    "user_id" equalTo user.uid
                    "food_item_id" equalTo itemId
                }
                .get()
            val document = querySnapshot.documents.firstOrNull() ?: return@withContext
            document.reference.delete()
        }
    }

    suspend fun clearCart() {
        return clearCartInternal()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun checkout(data: OrderDataDto, items: List<CartItem>): OrderInfoDto {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: throw IllegalStateException("User not logged in")
            val batch = firestore.batch()

            // Create order document
            val orderDocRef = firestore.collection("orders")
                .document
            val orderData = mapOf(
                "order_id" to data.id,
                "user_id" to user.uid,
                "order_time" to data.time,
                "comments" to data.comments,
                "items" to items.map { item ->
                    mapOf(
                        "food_item_id" to item.foodItemWithCount.foodItem.id,
                        "quantity" to item.foodItemWithCount.count,
                        "toppings" to item.toppingsWithCount.map {
                            mapOf(
                                "topping_id" to it.topping.id,
                                "quantity" to it.count
                            )
                        }
                    )
                },
                "timestamp" to Timestamp.now()
            )
            batch.set(orderDocRef, orderData)

            // Clear cart
            clearCartInternal(batch)
            OrderInfoDto(
                id = data.id,
                time = data.time
            )
        }
    }

    private suspend fun clearCartInternal(batch: WriteBatch = firestore.batch()) {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext
            val items = firestore.collection("cart_items")
                .where {
                    "user_id" equalTo user.uid
                }
                .get()
                .documents

            for (item in items) {
                batch.delete(item.reference)
            }
            batch.commit()
        }
    }

    suspend fun getOrders(): List<OrderHistoryDto> {
        return withContext(Dispatchers.IO) {
            val user = auth.currentUser ?: return@withContext emptyList()
            val documents = firestore.collection("orders")
                .where {
                    "user_id" equalTo user.uid
                }
                .get()
                .documents

            val orders = documents.map { doc ->
                doc.data<OrderHistoryDto>()
            }
            orders
        }
    }

}

@Serializable
data class CartItemRemoteDto(
    @SerialName("user_id")
    val userId: String,
    @SerialName("food_item_id")
    val foodItemId: String,
    val quantity: Int,
    val toppings: List<ToppingRemoteDto>,
)

@Serializable
data class ToppingRemoteDto(
    @SerialName("topping_id")
    val toppingId: String,
    val quantity: Int,
)