package com.seenu.dev.android.lazypizza.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.state.OrderStatus
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.drinks
import lazypizza.composeapp.generated.resources.ice_cream
import lazypizza.composeapp.generated.resources.pizza
import lazypizza.composeapp.generated.resources.sauces
import lazypizza.composeapp.generated.resources.status_cancelled
import lazypizza.composeapp.generated.resources.status_completed
import lazypizza.composeapp.generated.resources.status_in_progress
import lazypizza.composeapp.generated.resources.status_pending
import org.jetbrains.compose.resources.StringResource

fun FoodType.getStringRes(): StringResource {
    return when (this) {
        FoodType.PIZZA -> Res.string.pizza
        FoodType.DRINK -> Res.string.drinks
        FoodType.SAUCE -> Res.string.sauces
        FoodType.ICE_CREAM -> Res.string.ice_cream
    }
}

fun OrderStatus.getStringRes(): StringResource {
    return when (this) {
        OrderStatus.PENDING -> Res.string.status_pending
        OrderStatus.IN_PROGRESS -> Res.string.status_in_progress
        OrderStatus.COMPLETED -> Res.string.status_completed
        OrderStatus.CANCELLED -> Res.string.status_cancelled
    }
}

@Composable
fun OrderStatus.getBackgroundColor(): Color {
    return when (this) {
        OrderStatus.PENDING -> Color(0xFFF9A825)
        OrderStatus.IN_PROGRESS -> Color(0xFFF9A825)
        OrderStatus.COMPLETED -> Color(0xFF2E7D32)
        OrderStatus.CANCELLED -> Color(0xFF2E7D32)
    }
}