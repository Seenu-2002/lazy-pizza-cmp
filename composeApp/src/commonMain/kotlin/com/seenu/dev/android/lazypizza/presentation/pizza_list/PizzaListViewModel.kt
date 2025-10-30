package com.seenu.dev.android.lazypizza.presentation.pizza_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger.Companion.e
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.model.CartItem
import com.seenu.dev.android.lazypizza.domain.model.FoodItemWithCount
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.mappers.toDomain
import com.seenu.dev.android.lazypizza.presentation.mappers.toUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodSection
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PizzaListViewModel constructor(
    private val repository: LazyPizzaRepository,
    private val cartRepository: LazyPizzaCartRepository
) : ViewModel() {

    private var items: List<FoodSection> = emptyList()

    private val _filteredItems: MutableStateFlow<UiState<FoodListUiState>> =
        MutableStateFlow(UiState.Empty())
    val filteredItems: StateFlow<UiState<FoodListUiState>> by lazy {
        _filteredItems.onStart {
            viewModelScope.launch {
                getFoodItems()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UiState.Empty()
        )
    }

    fun handleEvent(event: PizzaListEvent) {
        when (event) {
            is PizzaListEvent.UpdateCountInCart -> {
                updateCountInCard(event.itemId, event.count)
            }

            is PizzaListEvent.Search -> {
                updateSearchQuery(event.query)
            }
        }
    }

    private fun getFoodItems() {
        viewModelScope.launch {
            _filteredItems.value = UiState.Loading()
            items = emptyList()
            try {
                cartRepository.getCartItemsFlow().collect { cartItems ->
                    val cartItemsMap = cartItems.associate { item -> item.foodItemWithCount.foodItem.id to item.foodItemWithCount.count }
                    val sections = repository.getFoodItems()
                        .groupBy { it.type }
                        .map {
                            FoodSection(
                                type = it.key,
                                items = it.value.map { item ->
                                    item.toUiModel(cartItemsMap[item.id] ?: 0)
                                }
                            )
                        }.sortedBy { it.type.ordinal }
                    val filters = sections.map { it.type }
                    _filteredItems.value = UiState.Success(
                        FoodListUiState(
                            sections = sections,
                            filters = filters
                        )
                    )
                }
            } catch (exp: Exception) {
                e(exp) { "Error fetching food items: ${exp.message}" }
                _filteredItems.value =
                    UiState.Error("Error fetching food items: ${exp.message}", exception = exp)
            }
        }
    }

    private fun updateCountInCard(itemId: String, count: Int) {
        viewModelScope.launch {
            val newSections = mutableListOf<FoodSection>()
            val items = (filteredItems.value as? UiState.Success)?.data?.sections
                ?: return@launch
            var isItemFound = false
            for (section in items) {
                if (isItemFound) {
                    newSections.add(section.copy())
                } else {

                    val item = section.items.find { it.id == itemId }
                    if (item != null) {
                        isItemFound = true
                        val newItem = item.copy(countInCart = count)
                        updateCart(item, item.countInCart, count)
                        val newItems = section.items.map {
                            if (item.id == it.id) newItem else it
                        }
                        newSections.add(section.copy(items = newItems))
                    } else {
                        newSections.add(section.copy())
                    }
                }
            }

            (_filteredItems.value as? UiState.Success)?.data?.let { state ->
                val newState =
                    state.copy(sections = newSections, filters = newSections.map { it.type })
                _filteredItems.value = UiState.Success(newState)
            } ?: e { "Previous state is not Success. Instead found: ${filteredItems.value}" }
        }
    }

    private fun updateCart(item: FoodItemUiModel, oldCount: Int, newCount: Int) {
        viewModelScope.launch {
            val cartItem = CartItem(
                foodItemWithCount = FoodItemWithCount(
                    foodItem = item.toDomain(),
                    count = newCount
                )
            )
            if (newCount == 0) {
                cartRepository.removeItemFromCart(item.id)
            } else if (oldCount == 0 && newCount > 0) {
                cartRepository.addItemToCart(cartItem)
            } else {
                cartRepository.updateItemInCart(cartItem)
            }
        }
    }


    private fun updateSearchQuery(query: String) {
        val newSections = if (query.isBlank()) {
            items
        } else {
            items.map { section ->
                val filteredItems =
                    section.items.filter { it.name.contains(query, ignoreCase = true) }
                section.copy(items = filteredItems)
            }.filter { it.items.isNotEmpty() }
        }
        val filters = newSections.map { it.type }
        val newState = FoodListUiState(
            sections = newSections,
            filters = filters,
            search = query
        )
        _filteredItems.value = UiState.Success(newState)
    }

}

sealed interface PizzaListEvent {
    data class UpdateCountInCart(val itemId: String, val count: Int) : PizzaListEvent
    data class Search(val query: String) : PizzaListEvent
}

data class FoodListUiState(
    val sections: List<FoodSection> = emptyList(),
    val filters: List<FoodType> = emptyList(),
    val search: String = ""
)
