package com.seenu.dev.android.lazypizza.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object PizzaList : Route

    @Serializable
    data class PizzaDetail constructor(val id: String) : Route
}