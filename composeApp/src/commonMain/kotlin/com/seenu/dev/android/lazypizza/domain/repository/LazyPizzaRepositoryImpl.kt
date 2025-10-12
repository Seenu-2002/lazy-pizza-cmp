package com.seenu.dev.android.lazypizza.domain.repository

import com.seenu.dev.android.lazypizza.data.dto.FoodItemDto
import com.seenu.dev.android.lazypizza.data.dto.ToppingDto
import com.seenu.dev.android.lazypizza.data.mappers.toDomain
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.Topping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LazyPizzaRepositoryImpl : LazyPizzaRepository {
    override suspend fun getFoodItems(): List<FoodItem> {
        return withContext(Dispatchers.IO) {
            delay((500L..1000L).random())
            Json.decodeFromString<List<FoodItemDto>>(MOCK_JSON_FOOD).map { it.toDomain() }
        }
    }

    override suspend fun getToppings(): List<Topping> {
        return withContext(Dispatchers.IO) {
            delay((500L..1000L).random())
            Json.decodeFromString<List<ToppingDto>>(MOCK_JSON_TOPPINGS).map { it.toDomain() }
        }
    }
}

private val MOCK_JSON_FOOD = """
    [
      {
        "id": 1,
        "name": "Margherita",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "fresh basil", "olive oil"],
        "price": 8.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Margherita_feiryf.png"
      },
      {
        "id": 2,
        "name": "Pepperoni",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "pepperoni"],
        "price": 9.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Pepperoni_ety6cd.png"
      },
      {
        "id": 3,
        "name": "Hawaiian",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "ham", "pineapple"],
        "price": 10.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Hawaiian_ztwobr.png"
      },
      {
        "id": 4,
        "name": "BBQ Chicken",
        "type": "PIZZA",
        "ingredients": ["BBQ sauce", "mozzarella", "grilled chicken", "onion", "corn"],
        "price": 11.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/BBQ_Chicken_jkwqor.png"
      },
      {
        "id": 5,
        "name": "Four Cheese",
        "type": "PIZZA",
        "ingredients": ["Mozzarella", "gorgonzola", "parmesan", "ricotta"],
        "price": 11.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Four_Cheese_huejuf.png"
      },
      {
        "id": 6,
        "name": "Veggie Delight",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "mushrooms", "olives", "bell pepper", "onion", "corn"],
        "price": 9.79,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595127/Veggie_Delight_xvjir3.png"
      },
      {
        "id": 7,
        "name": "Meat Lovers",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "pepperoni", "ham", "bacon", "sausage"],
        "price": 12.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/Meat_Lovers_pz9cah.png"
      },
      {
        "id": 8,
        "name": "Spicy Inferno",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "spicy salami", "jalapeños", "red chili pepper", "garlic"],
        "price": 11.29,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595126/Spicy_Inferno_mz1ddl.png"
      },
      {
        "id": 9,
        "name": "Seafood Special",
        "type": "PIZZA",
        "ingredients": ["Tomato sauce", "mozzarella", "shrimp", "mussels", "squid", "parsley"],
        "price": 13.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595125/Seafood_Special_k4urjr.png"
      },
      {
        "id": 10,
        "name": "Truffle Mushroom",
        "type": "PIZZA",
        "ingredients": ["Cream sauce", "mozzarella", "mushrooms", "truffle oil", "parmesan"],
        "price": 12.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595129/Truffle_Mushroom_mbs6cg.png"
      },
      {
        "id": 11,
        "name": "Mineral Water",
        "type": "DRINK",
        "ingredients": [],
        "price": 1.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595126/mineral_water_hwbjbv.png"
      },
      {
        "id": 12,
        "name": "7-Up",
        "type": "DRINK",
        "ingredients": [],
        "price": 1.89,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595126/7-up_conszk.png"
      },
      {
        "id": 13,
        "name": "Pepsi",
        "type": "DRINK",
        "ingredients": [],
        "price": 1.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595125/pepsi_rbzmx1.png"
      },
      {
        "id": 14,
        "name": "Orange Juice",
        "type": "DRINK",
        "ingredients": [],
        "price": 2.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595125/orange_juice_tdpptc.png"
      },
      {
        "id": 15,
        "name": "Apple Juice",
        "type": "DRINK",
        "ingredients": [],
        "price": 2.29,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595126/apple_juice_d33zxd.png"
      },
      {
        "id": 16,
        "name": "Iced Tea (Lemon)",
        "type": "DRINK",
        "ingredients": [],
        "price": 2.19,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595126/iced_tea_fxyjlr.png"
      },
      {
        "id": 17,
        "name": "Garlic Sauce",
        "type": "SAUCE",
        "ingredients": [],
        "price": 0.59,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595129/Garlic_Sauce_n7otqj.png"
      },
      {
        "id": 18,
        "name": "BBQ Sauce",
        "type": "SAUCE",
        "ingredients": [],
        "price": 0.59,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595128/BBQ_Sauce_nkr8ao.png"
      },
      {
        "id": 19,
        "name": "Cheese Sauce",
        "type": "SAUCE",
        "ingredients": [],
        "price": 0.89,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595129/Cheese_Sauce_bhddzc.png"
      },
      {
        "id": 20,
        "name": "Spicy Chili Sauce",
        "type": "SAUCE",
        "ingredients": [],
        "price": 0.59,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595129/Spicy_Chili_Sauce_bukcis.png"
      },
      {
        "id": 21,
        "name": "Vanilla Ice Cream",
        "type": "ICE_CREAM",
        "ingredients": [],
        "price": 2.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/vanilla_th5czo.png"
      },
      {
        "id": 22,
        "name": "Chocolate Ice Cream",
        "type": "ICE_CREAM",
        "ingredients": [],
        "price": 2.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595132/chocolate_obrqge.png"
      },
      {
        "id": 23,
        "name": "Strawberry Ice Cream",
        "type": "ICE_CREAM",
        "ingredients": [],
        "price": 2.49,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595132/strawberry_vhcsvv.png"
      },
      {
        "id": 24,
        "name": "Cookies Ice Cream",
        "type": "ICE_CREAM",
        "ingredients": [],
        "price": 2.79,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/cookies_l3pf8z.png"
      },
      {
        "id": 25,
        "name": "Pistachio Ice Cream",
        "type": "ICE_CREAM",
        "ingredients": [],
        "price": 2.99,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595132/pistachio_ap6a9q.png"
      },
      {
        "id": 26,
        "name": "Mango Sorbet",
        "type": "ICE_CREAM",
        "ingredients": [],
        "price": 2.69,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/mango_sorbet_yzjqft.png"
      }
    ]

""".trimIndent()

private val MOCK_JSON_TOPPINGS = """
    [
      {
        "id": 27,
        "name": "Bacon",
        "price": 1,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/bacon_jsutui.png"
      },
      {
        "id": 28,
        "name": "Extra Cheese",
        "price": 1,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/cheese_ebyftk.png"
      },
      {
        "id": 29,
        "name": "Corn",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/corn_atu9xo.png"
      },
      {
        "id": 30,
        "name": "Tomato",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/tomato_zxv5ws.png"
      },
      {
        "id": 31,
        "name": "Olives",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/olive_bdxnsj.png"
      },
      {
        "id": 32,
        "name": "Pepperoni",
        "price": 1,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595129/pepperoni_zr0rxm.png"
      },
      {
        "id": 33,
        "name": "Mushrooms",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/mashroom_vb2evw.png"
      },
      {
        "id": 34,
        "name": "Basil",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/basil_x1efyb.png"
      },
      {
        "id": 35,
        "name": "Pineapple",
        "price": 1,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/pineapple_b2ua3x.png"
      },
      {
        "id": 36,
        "name": "Onion",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595130/onion_owmrhy.png"
      },
      {
        "id": 37,
        "name": "Chili Peppers",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/chilli_wm4ain.png"
      },
      {
        "id": 38,
        "name": "Spinach",
        "price": 0.5,
        "imageUrl": "https://res.cloudinary.com/dzfevhkfl/image/upload/v1759595131/spinach_qdwsh8.png"
      }
    ]
""".trimIndent()