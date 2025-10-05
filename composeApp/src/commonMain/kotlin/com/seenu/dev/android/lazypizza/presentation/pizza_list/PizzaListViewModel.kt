package com.seenu.dev.android.lazypizza.presentation.pizza_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodSection
import com.seenu.dev.android.lazypizza.presentation.state.FoodType
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class PizzaListViewModel : ViewModel() {

    private val _items: MutableStateFlow<UiState<List<FoodSection>>> =
        MutableStateFlow(UiState.Empty())
    val items: StateFlow<UiState<List<FoodSection>>> = _items.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            val mockSections = generateMockFoodSections()
            _items.value = UiState.Success(mockSections)
        }
    }

}

@OptIn(ExperimentalTime::class)
fun generateMockFoodSections(): List<FoodSection> {
    val random = Random(Clock.System.now().toEpochMilliseconds())

    fun randomIngredients(): String {
        val ingredients = listOf(
            "Cheese", "Tomato", "Basil", "Pepperoni", "Onion",
            "Olives", "Mushrooms", "Bacon", "Garlic", "Pineapple"
        )
        return List(random.nextInt(2, 5)) { ingredients.random(random) }
            .joinToString(", ")
    }

    fun randomPrice(type: FoodType): Double = when (type) {
        FoodType.PIZZA -> random.nextDouble(6.0, 15.0)
        FoodType.DRINK -> random.nextDouble(1.5, 5.0)
        FoodType.SAUCE -> random.nextDouble(0.5, 2.0)
        FoodType.ICE_CREAM -> random.nextDouble(2.0, 6.0)
    }

    fun generateItems(type: FoodType): List<FoodItemUiModel> {
        val count = random.nextInt(5, 21)
        return List(count) { index ->
            FoodItemUiModel(
                id = "${type.name}_$index".hashCode().toLong(),
                name = when (type) {
                    FoodType.PIZZA -> listOf("Margherita", "Pepperoni", "Veggie", "BBQ Chicken", "Hawaiian").random(random)
                    FoodType.DRINK -> listOf("Coke", "Sprite", "Fanta", "Iced Tea", "Lemonade").random(random)
                    FoodType.SAUCE -> listOf("Ketchup", "Mayo", "Garlic Dip", "BBQ", "Mustard").random(random)
                    FoodType.ICE_CREAM -> listOf("Vanilla", "Chocolate", "Strawberry", "Mango", "Mint").random(random)
                },
                type = type,
                ingredients = randomIngredients(),
                prize = randomPrice(type).let { (it * 100).toInt() / 100.0 }, // Round to 2 decimal places
                countInCart = 0
            )
        }
    }

    return FoodType.entries.map { type ->
        FoodSection(type = type, items = generateItems(type))
    }
}







