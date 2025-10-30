package com.seenu.dev.android.lazypizza.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.presentation.mappers.toDomain
import com.seenu.dev.android.lazypizza.presentation.mappers.toUiModel
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PizzaCartViewModel constructor(
    private val repository: LazyPizzaCartRepository,
    private val cartRepository: LazyPizzaCartRepository
) : ViewModel() {

    private val _cart: MutableStateFlow<UiState<CartUiState>> = MutableStateFlow(UiState.Empty())
    val cart: StateFlow<UiState<CartUiState>> = _cart.asStateFlow()

    private val _events: MutableSharedFlow<CartSideEffect> = MutableSharedFlow()
    val events: MutableSharedFlow<CartSideEffect> = _events

    fun onIntent(intent: CartIntent) {
        viewModelScope.launch {
            when (intent) {
                is CartIntent.GetCartItems -> {
                    repository.getCartItemsFlow().collect { cartItems ->
                        _cart.value = UiState.Success(
                            CartUiState(
                                items = cartItems.map { it.toUiModel() }.sortedBy { it.foodItem.type.sortOrder }
                            ))
                    }
                }

                is CartIntent.DeleteItem -> {
                    val cartData = (_cart.value as? UiState.Success)?.data
                        ?: return@launch
                    val updatedCart =
                        cartData.items.filterNot { it.foodItem.id == intent.itemId }
                    cartRepository.removeItemFromCart(itemId = intent.itemId)
                    _cart.value = UiState.Success(CartUiState(updatedCart))
                }

                is CartIntent.UpdateItemQuantity -> {
                    val cartData = (_cart.value as? UiState.Success)?.data
                        ?: return@launch
                    var updatedItem: CartItemUiModel? = null
                    val updatedCart = cartData.items.map {
                        if (it.foodItem.id == intent.itemId) {
                            val updatedFoodItem =
                                it.foodItem.copy(countInCart = intent.quantity)
                            updatedItem = it.copy(foodItem = updatedFoodItem)
                            it.copy(foodItem = updatedFoodItem)
                        } else {
                            it
                        }
                    }
                    updatedItem?.let {
                        cartRepository.updateItemInCart(
                            it.toDomain()
                        )
                    }
                    _cart.value = UiState.Success(CartUiState(updatedCart))
                }

                is CartIntent.Checkout -> {
                    // TODO: Checkout implementation
                    _events.emit(CartSideEffect.Checkout)
                }
            }
        }
    }

}

data class CartUiState constructor(
    val items: List<CartItemUiModel>
) {

    val total = calculateTotalPrice()

    private fun calculateTotalPrice(): Double {
        return items.sumOf {
            it.foodItem.countInCart * it.foodItem.price +
                    it.toppings.sumOf { topping -> topping.countInCart * topping.price }
        }
    }
}

sealed interface CartIntent {
    data class DeleteItem constructor(val itemId: String) : CartIntent
    data class UpdateItemQuantity constructor(val itemId: String, val quantity: Int) : CartIntent
    data object Checkout : CartIntent
    data object GetCartItems : CartIntent
}

sealed interface CartSideEffect {
    data object Checkout : CartSideEffect
}