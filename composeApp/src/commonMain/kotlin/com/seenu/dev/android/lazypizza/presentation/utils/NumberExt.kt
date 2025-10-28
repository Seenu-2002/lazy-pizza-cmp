package com.seenu.dev.android.lazypizza.presentation.utils

import kotlin.math.round

fun Double.roundTo2Digits(): Double {
    return (round(this * 100) / 100)
}