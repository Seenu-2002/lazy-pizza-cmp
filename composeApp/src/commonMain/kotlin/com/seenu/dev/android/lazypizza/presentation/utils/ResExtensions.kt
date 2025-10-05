package com.seenu.dev.android.lazypizza.presentation.utils

import com.seenu.dev.android.lazypizza.presentation.state.FoodType
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.drinks
import lazypizza.composeapp.generated.resources.ice_cream
import lazypizza.composeapp.generated.resources.pizza
import lazypizza.composeapp.generated.resources.sauces
import org.jetbrains.compose.resources.StringResource

fun FoodType.getStringRes(): StringResource {
    return when (this) {
        FoodType.PIZZA -> Res.string.pizza
        FoodType.DRINK -> Res.string.drinks
        FoodType.SAUCE -> Res.string.sauces
        FoodType.ICE_CREAM -> Res.string.ice_cream
    }
}