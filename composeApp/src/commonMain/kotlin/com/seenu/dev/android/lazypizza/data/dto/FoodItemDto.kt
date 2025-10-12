package com.seenu.dev.android.lazypizza.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodItemDto constructor(
    val id: Long,
    val name: String,
    val type: String,
    val ingredients: List<String>,
    val price: Double,
    val imageUrl: String,
)