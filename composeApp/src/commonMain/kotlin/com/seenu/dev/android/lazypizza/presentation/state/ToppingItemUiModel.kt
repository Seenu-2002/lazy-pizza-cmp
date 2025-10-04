package com.seenu.dev.android.lazypizza.presentation.state

data class ToppingItemUiModel constructor(
    val id: Long,
    val name: String,
    val prize: Double,
    val prizeLabel: String
)