package com.seenu.dev.android.lazypizza.domain.repository

import com.seenu.dev.android.lazypizza.data.dto.FoodItemDto
import com.seenu.dev.android.lazypizza.data.dto.ToppingDto
import com.seenu.dev.android.lazypizza.data.mappers.toDomain
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.model.FoodItem
import com.seenu.dev.android.lazypizza.domain.model.Topping
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class FirebaseLazyPizzaRepository constructor() : LazyPizzaRepository {

    private val firestore = Firebase.firestore
    val json = Json { ignoreUnknownKeys = true }

    override suspend fun getFoodItems(): List<FoodItem> {
        return withContext(Dispatchers.IO) {
            firestore.collection("food_items")
                .get()
                .documents
                .map { it.data<FoodItemDto>().copy(id = it.id).toDomain() }
        }
    }

    override suspend fun getFoodItemById(id: String): FoodItem? {
        return withContext(Dispatchers.IO) {
            firestore.collection("food_items")
                .document(id)
                .get()
                .data<FoodItemDto>()
                .copy(id = id)
                .toDomain()
        }
    }

    override suspend fun getToppings(): List<Topping> {
        return withContext(Dispatchers.IO) {
            firestore.collection("toppings")
                .get()
                .documents
                .map { it.data<ToppingDto>().copy(id = it.id).toDomain() }
        }
    }
}