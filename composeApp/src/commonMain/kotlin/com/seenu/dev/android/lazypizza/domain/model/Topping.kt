package com.seenu.dev.android.lazypizza.domain.model

data class Topping constructor(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
)