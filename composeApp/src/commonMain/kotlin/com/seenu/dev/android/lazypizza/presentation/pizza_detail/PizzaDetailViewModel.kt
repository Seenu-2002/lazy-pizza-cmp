package com.seenu.dev.android.lazypizza.presentation.pizza_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.e
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.presentation.mappers.toUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.ToppingUiModel
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PizzaDetailViewModel constructor(
    private val repository: LazyPizzaRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<PizzaDetailUiState>> =
        MutableStateFlow(UiState.Empty())
    val uiState: StateFlow<UiState<PizzaDetailUiState>> = _uiState.asStateFlow()

    fun handleEvent(event: PizzaDetailEvent) {
        when (event) {
            is PizzaDetailEvent.LoadPizzaDetail -> {
                loadPizzaDetail(event.id)
            }

            is PizzaDetailEvent.AddTopping -> {
                addTopping(event.topping)
            }

            is PizzaDetailEvent.RemoveTopping -> {
                removeTopping(event.topping)
            }
        }
    }

    private fun loadPizzaDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading()
            try {
                val pizza: FoodItem = repository.getFoodItemById(id)
                    ?: throw IllegalArgumentException("Pizza not found")
                val toppings = repository.getToppings()
                val pizzaDetailUiState = PizzaDetailUiState(
                    pizza = pizza.toUiModel(),
                    toppings = toppings.map { it.toUiModel() }
                )
                _uiState.value = UiState.Success(pizzaDetailUiState)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(message = e.message ?: "", e)
            }
        }
    }

    private fun addTopping(topping: ToppingUiModel) {
        updateToppingCount(topping, topping.countInCart + 1)
    }

    private fun removeTopping(topping: ToppingUiModel) {
        if (topping.countInCart > 0) {
            updateToppingCount(topping, topping.countInCart - 1)
        }
    }

    private fun updateToppingCount(topping: ToppingUiModel, count: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is UiState.Success) {
                val currentToppings = currentState.data.toppings.toMutableList()
                val index = currentToppings.indexOfFirst { it.id == topping.id }
                if (index != -1) {
                    val updatedTopping = currentToppings[index].copy(countInCart = count)
                    currentToppings[index] = updatedTopping
                    val updatedUiState = currentState.data.copy(toppings = currentToppings)
                    _uiState.value = UiState.Success(updatedUiState)
                }
            } else {
                e { "Cannot update topping count when state is not Success"}
            }
        }
    }

}

data class PizzaDetailUiState constructor(
    val pizza: FoodItemUiModel,
    val toppings: List<ToppingUiModel>
) {
    val cartTotal: Double
        get() {
            val toppingsTotal = toppings.sumOf { it.price * it.countInCart }
            return pizza.price + toppingsTotal
        }
}

sealed interface PizzaDetailEvent {
    data class LoadPizzaDetail(val id: String) : PizzaDetailEvent
    data class AddTopping(val topping: ToppingUiModel) : PizzaDetailEvent
    data class RemoveTopping(val topping: ToppingUiModel) : PizzaDetailEvent
}