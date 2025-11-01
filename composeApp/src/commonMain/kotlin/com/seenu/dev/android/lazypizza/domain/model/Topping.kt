package com.seenu.dev.android.lazypizza.domain.model

data class Topping constructor(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
)

data class ToppingWithCount(
    val topping: Topping,
    val count: Int,
)