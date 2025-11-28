package com.seenu.dev.android.lazypizza.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object PizzaList : Route

    @Serializable
    data class PizzaDetail constructor(val id: String) : Route

    @Serializable
    data object Cart : Route

    @Serializable
    data object History : Route

    @Serializable
    data class Login constructor(val from: From) : Route {

        @Serializable
        enum class From  {
            HISTORY,
            LIST
        }
    }

    @Serializable
    data object Checkout : Route

}