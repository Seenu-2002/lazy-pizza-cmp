package com.seenu.dev.android.lazypizza.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class FoodItemDto constructor(
    @Transient
    val id: String = "",
    val name: String,
    val type: String,
    val ingredients: List<String>,
    val price: Double,
    @SerialName("image_url")
    val imageUrl: String,
)