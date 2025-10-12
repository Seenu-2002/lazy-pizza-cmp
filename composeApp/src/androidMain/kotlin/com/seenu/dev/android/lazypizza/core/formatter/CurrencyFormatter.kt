package com.seenu.dev.android.lazypizza.core.formatter

actual class CurrencyFormatter {

    actual fun format(amount: Double): String {
        return "$${"%.2f".format(amount)}"
    }

}