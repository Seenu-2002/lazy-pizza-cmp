package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions constructor(
    val listScreen: PizzaListScreenDimensions,
    val navBar: NavBarDimensions,
    val historyScreen: HistoryScreenDimensions
)

data class PizzaListScreenDimensions constructor(
    val gridCount: Int,
    val bannerHeight: Dp
)

data class NavBarDimensions constructor(
    val iconLabelSpacing: Dp
)

data class HistoryScreenDimensions constructor(
    val columnCount: Int
)

val mobileDimensions = Dimensions(
    listScreen = PizzaListScreenDimensions(
        gridCount = 1,
        bannerHeight = 140.dp
    ),
    navBar = NavBarDimensions(iconLabelSpacing = 2.dp),
    historyScreen = HistoryScreenDimensions(columnCount = 1)
)

val tabletDimensions = Dimensions(
    listScreen = PizzaListScreenDimensions(
        gridCount = 2,
        bannerHeight = 160.dp
    ),
    navBar = NavBarDimensions(iconLabelSpacing = 0.dp),
    historyScreen = HistoryScreenDimensions(columnCount = 2)
)