package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions constructor(
    val listScreen: PizzaListScreenDimensions,
    val navBar: NavBarDimensions
)

data class PizzaListScreenDimensions constructor(
    val gridCount: Int,
    val bannerHeight: Dp
)

data class NavBarDimensions constructor(
    val iconLabelSpacing: Dp
)

val mobileDimensions = Dimensions(
    listScreen = PizzaListScreenDimensions(
        gridCount = 1,
        bannerHeight = 150.dp
    ),
    navBar = NavBarDimensions(iconLabelSpacing = 2.dp)
)

val tabletDimensions = Dimensions(
    listScreen = PizzaListScreenDimensions(
        gridCount = 2,
        bannerHeight = 160.dp
    ),
    navBar = NavBarDimensions(iconLabelSpacing = 0.dp)
)