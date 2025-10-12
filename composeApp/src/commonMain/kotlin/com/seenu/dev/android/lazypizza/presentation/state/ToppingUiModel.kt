package com.seenu.dev.android.lazypizza.presentation.state

data class ToppingUiModel constructor(
    val id: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val countInCart: Int = 0
)