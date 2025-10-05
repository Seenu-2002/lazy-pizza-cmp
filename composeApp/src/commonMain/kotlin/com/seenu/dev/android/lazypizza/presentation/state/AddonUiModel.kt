package com.seenu.dev.android.lazypizza.presentation.state

data class AddonUiModel constructor(
    val id: Long,
    val name: String,
    val prize: Double,
    val countInCart: Int = 0
)