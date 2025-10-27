package com.seenu.dev.android.lazypizza.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.presentation.mappers.toUiModel
import com.seenu.dev.android.lazypizza.presentation.state.CartItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PizzaCartViewModel constructor(
    private val repository: LazyPizzaCartRepository
) : ViewModel() {

    private val _cart: MutableStateFlow<UiState<CartUiState>> = MutableStateFlow(UiState.Empty())
    val cart: StateFlow<UiState<CartUiState>> = _cart.asStateFlow()

    private val _events: MutableSharedFlow<CartSideEffect> = MutableSharedFlow()
    val events: MutableSharedFlow<CartSideEffect> = _events

    fun onIntent(intent: CartIntent) {
        viewModelScope.launch {
            val cartData = (_cart.value as? UiState.Success)?.data
                ?: return@launch
            when (intent) {
                is CartIntent.GetCartItems -> {
                    val cartItems = repository.getCartItems()
                    _cart.value = UiState.Success(
                        CartUiState(
                        items = cartItems.map { it.toUiModel() }
                    ))
                }

                is CartIntent.DeleteItem -> {
                    val updatedCart =
                        cartData.items.filterNot { it.foodItemUiModel.id == intent.itemId }
                    // TODO: Update cart
                    _cart.value = UiState.Success(CartUiState(updatedCart))
                }

                is CartIntent.UpdateItemQuantity -> {
                    val updatedCart = cartData.items.map {
                        if (it.foodItemUiModel.id == intent.itemId) {
                            val updatedFoodItem =
                                it.foodItemUiModel.copy(countInCart = intent.quantity)
                            it.copy(foodItemUiModel = updatedFoodItem)
                        } else {
                            it
                        }
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

    private val total = calculateTotalPrice()

    private fun calculateTotalPrice(): Double {
        return items.sumOf {
            it.foodItemUiModel.countInCart * it.foodItemUiModel.price +
                    it.toppingsUiModel.sumOf { topping -> topping.countInCart * topping.price }
        }
    }
}

sealed interface CartIntent {
    data class DeleteItem(val itemId: String) : CartIntent
    data class UpdateItemQuantity(val itemId: String, val quantity: Int) : CartIntent
    data object Checkout : CartIntent
    data object GetCartItems : CartIntent
}

sealed interface CartSideEffect {
    data object Checkout : CartSideEffect
}