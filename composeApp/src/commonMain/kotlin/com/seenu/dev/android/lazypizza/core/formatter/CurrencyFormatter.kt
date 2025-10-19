package com.seenu.dev.android.lazypizza.core.formatter

expect class CurrencyFormatter constructor() {
    fun format(amount: Double): String
}