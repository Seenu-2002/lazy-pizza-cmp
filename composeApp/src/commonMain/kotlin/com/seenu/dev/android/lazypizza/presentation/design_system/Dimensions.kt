package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions constructor(
    val listScreen: PizzaListScreenDimensions
)

data class PizzaListScreenDimensions constructor(
    val gridCount: Int,
    val bannerHeight: Dp
)

val mobileDimensions = Dimensions(
    listScreen = PizzaListScreenDimensions(
        gridCount = 1,
        bannerHeight = 150.dp
    )
)

val tabletDimensions = Dimensions(
    listScreen = PizzaListScreenDimensions(
        gridCount = 2,
        bannerHeight = 160.dp
    )
)